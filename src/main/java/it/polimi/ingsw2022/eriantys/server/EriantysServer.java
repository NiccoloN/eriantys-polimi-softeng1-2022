package it.polimi.ingsw2022.eriantys.server;

import it.polimi.ingsw2022.eriantys.messages.toClient.*;
import it.polimi.ingsw2022.eriantys.messages.Message;
import it.polimi.ingsw2022.eriantys.messages.toServer.GameSettings;
import it.polimi.ingsw2022.eriantys.messages.toServer.ToServerMessage;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 * @author Niccolò Nicolosi
 * @author Francesco Melegati Maccari
 * @author Emanuele Musto
 */
public class EriantysServer {

    public static final int PORT_NUMBER = 65000;

    public static void launch(String[] args) throws IOException, InterruptedException {

        createInstance();
        getInstance().start();
    }

    private static EriantysServer instance;

    private static void createInstance() throws IOException {

        instance = new EriantysServer();
    }

    public static EriantysServer getInstance() {

        return instance;
    }

    private final ServerSocket serverSocket;

    private final Map<String, Socket> clients;
    private final Map<Socket, ObjectOutputStream> clientOutputStreams;
    private final Map<Socket, ObjectInputStream> clientInputStreams;
    private GameSettings gameSettings;

    private boolean running;

    private EriantysServer() throws IOException {

        //open server
        serverSocket = new ServerSocket(PORT_NUMBER);

        //initialize client maps
        clients = new LinkedHashMap<>(4);
        clientOutputStreams = new HashMap<>(4);
        clientInputStreams = new HashMap<>(4);
    }

    private void start() throws IOException, InterruptedException {

        running = true;
        System.out.println("\nServer started\nWaiting for the first player to join...");

        Socket firstClient = acceptConnection();
        requestGameSettings(firstClient);

        synchronized (this) {

            while (gameSettings == null) wait();
            sendToClient(new GameJoinedMessage(clients.keySet().toArray(new String[0]), gameSettings), firstClient);
            notifyAll();
        }

        for (int n = 1; n < gameSettings.numberOfPlayers; n++) {

            Socket currentClient = acceptConnection();

            synchronized (clients) {

                while (clients.size() <= n) clients.wait();

                String[] playerUsernames = clients.keySet().toArray(new String[0]);
                sendToClient(new GameJoinedMessage(playerUsernames, gameSettings), currentClient);

                for (Socket client : clients.values())
                    if (client != currentClient)
                        sendToClient(new NewPlayerJoinedMessage(playerUsernames), client);

                clients.notifyAll();
            }
        }
    }

    private Socket acceptConnection() throws IOException {

        //accept a connection
        Socket client = serverSocket.accept();
        System.out.println("Connection established with client " + client);

        //initialize object the streams of the accepted client
        clientOutputStreams.put(client, new ObjectOutputStream(client.getOutputStream()));
        clientInputStreams.put(client, new ObjectInputStream(client.getInputStream()));

        //notify the client of the successful connection
        sendToClient(new ConnectedMessage(), client);

        //ask the connected client to provide a username
        Message sent = new ChooseUsernameMessage();
        sendToClient(sent, client);

        listenToClient(client);

        return client;
    }

    private void requestGameSettings(Socket client) throws InterruptedException, IOException {

        synchronized(clients) {

            while(clients.size() == 0) clients.wait();
            sendToClient(new ChooseGameSettingsMessage(), client);
            clients.notifyAll();
        }
    }

    private void listenToClient(Socket client) {

        new Thread(() -> {

            while(running) {

                try {

                    Optional<ToServerMessage> message = readMessageFromClient(client);
                    if(message.isPresent()) message.get().manageAndReply(client);
                }
                catch(IOException | ClassNotFoundException e) {

                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void sendToClient(Message message, Socket clientSocket) throws IOException {

        ObjectOutputStream outputStream = clientOutputStreams.get(clientSocket);
        synchronized(outputStream) { outputStream.writeObject(message); }
    }

    private Optional<ToServerMessage> readMessageFromClient(Socket clientSocket) throws IOException, ClassNotFoundException {

        try {

            ToServerMessage message = (ToServerMessage) clientInputStreams.get(clientSocket).readObject();
            return Optional.of(message);
        }
        catch(EOFException e) {

            return Optional.empty();
        }
    }

    public boolean isAvailableUsername(String username) {

        return !clients.containsKey(username);
    }

    public void addClient(Socket clientSocket, String username) {

        if(clients.containsValue(clientSocket)) throw new RuntimeException("Client already connected");

        synchronized(clients) {

            clients.put(username, clientSocket);
            System.out.println("Added client with username: " + username);
            clients.notifyAll();
        }
    }

    public synchronized void addGameSettings(GameSettings gameSettings) {

        this.gameSettings = gameSettings;
        notifyAll();
    }
}
