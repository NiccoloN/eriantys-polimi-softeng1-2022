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

    public static void launch(String[] args) throws IOException {

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

    private Socket currentlyConnectingClient;
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

    private void start() {

        running = true;
        System.out.println("\nServer started\nWaiting for the first player to join...");

        new Thread(() -> {

            while(running) {

                try {

                    //accept a connection
                    currentlyConnectingClient = serverSocket.accept();
                    System.out.println("Connection established with client " + currentlyConnectingClient);

                    //initialize object the streams of the accepted client
                    clientOutputStreams.put(currentlyConnectingClient, new ObjectOutputStream(currentlyConnectingClient.getOutputStream()));
                    clientInputStreams.put(currentlyConnectingClient, new ObjectInputStream(currentlyConnectingClient.getInputStream()));

                    //notify the client of the successful connection
                    sendToClient(new ConnectedMessage(), currentlyConnectingClient);

                    //ask the connected client to provide a username
                    Message sent = new ChooseUsernameMessage();
                    sendToClient(sent, currentlyConnectingClient);

                    Optional<Message> response = waitForValidResponse(currentlyConnectingClient, sent, 120000);

                    if(response.isPresent()) {

                        System.out.println("Response received: " + response.get().getClass().getSimpleName());
                        response.get().manageAndReply();
                    }
                    else {

                        System.out.println("No valid response received in time: shutting down the server");
                        running = false;
                    }

                    currentlyConnectingClient = null;
                }
                catch(IOException | ClassNotFoundException e) {

                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void sendToClient(Message message, Socket clientSocket) throws IOException {

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

    public void addCurrentlyConnectingClient(String username) {

        if(currentlyConnectingClient == null) throw new RuntimeException("No client is currently connecting");
        clients.put(username, currentlyConnectingClient);
    }
}
