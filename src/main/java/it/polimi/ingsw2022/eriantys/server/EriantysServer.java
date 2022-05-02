package it.polimi.ingsw2022.eriantys.server;

import it.polimi.ingsw2022.eriantys.messages.toClient.ConnectedMessage;
import it.polimi.ingsw2022.eriantys.messages.toServer.AbortMessage;
import it.polimi.ingsw2022.eriantys.messages.toClient.ChooseUsernameMessage;
import it.polimi.ingsw2022.eriantys.messages.Message;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class EriantysServer {

    public static final int PORT_NUMBER = 65000;

    public static void launch(String[] args) throws IOException, ClassNotFoundException {

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

    private boolean running;

    private EriantysServer() throws IOException {

        //open server
        serverSocket = new ServerSocket(PORT_NUMBER);

        //initialize client maps
        clients = new HashMap<>(4);
        clientOutputStreams = new HashMap<>(4);
        clientInputStreams = new HashMap<>(4);
    }

    private void start() throws IOException, ClassNotFoundException {

        running = true;
        System.out.println("\nServer started\nWaiting for the first player to join...");

        acceptConnection();
        setupGame();
    }

    private void acceptConnection() throws IOException, ClassNotFoundException {

        //accept a connection
        Socket socket = serverSocket.accept();
        System.out.println("Connection established with client " + socket);

        //initialize object the streams of the accepted client
        clientOutputStreams.put(socket, new ObjectOutputStream(socket.getOutputStream()));
        clientInputStreams.put(socket, new ObjectInputStream(socket.getInputStream()));

        //notify the client of the successful connection
        sendToClient(new ConnectedMessage(), socket);

        //ask the connected client to provide a username
        Message sent = new ChooseUsernameMessage();
        sendToClient(sent, socket);

        Optional<Message> response = waitForValidResponse(socket, sent, 120000);

        if(response.isPresent()) {

            System.out.println("Response received: " + response.get().getClass().getSimpleName());
            response.get().manageAndReply(socket);
        }
        else {

            System.out.println("No valid response received in time: shutting down the server");
            running = false;
        }
    }

    private void setupGame() {}

    public void sendToClient(Message message, Socket clientSocket) throws IOException {

        clientOutputStreams.get(clientSocket).writeObject(message);
    }

    private Optional<Message> readMessageFromClient(Socket clientSocket, int timeout) throws IOException, ClassNotFoundException {

        try {

            clientSocket.setSoTimeout(timeout);
            Message message = (Message) clientInputStreams.get(clientSocket).readObject();
            return Optional.of(message);
        }
        catch(SocketTimeoutException | EOFException e) {

            return Optional.empty();
        }
    }

    private Optional<Message> waitForValidResponse(Socket clientSocket, Message sentMessage, int maxResponseTime) throws IOException, ClassNotFoundException {

        Optional<Message> message = Optional.empty();
        int elapsedTime = 0;
        long start;

        while((message.isEmpty() || !sentMessage.isValidResponse(message.get())) && elapsedTime < maxResponseTime) {

            start = System.nanoTime();
            message = readMessageFromClient(clientSocket, maxResponseTime - elapsedTime);
            elapsedTime += (System.nanoTime() - start) / 1000000;

            //if an abort message is received, stop waiting for a response
            if(message.isPresent() && message.get() instanceof AbortMessage) {

                System.out.println("Operation aborted from client");
                running = false;
                break;
            }
        }

        return message;
    }

    public boolean isAvailableUsername(String username) {

        return !clients.containsKey(username);
    }

    public void addClient(Socket clientSocket, String username) {

        if(clients.containsValue(clientSocket)) throw new RuntimeException("Client already connected");
        clients.put(username, clientSocket);
    }
}
