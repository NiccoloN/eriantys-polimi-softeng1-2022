package it.polimi.ingsw2022.eriantys.server;

import it.polimi.ingsw2022.eriantys.messages.Message;
import it.polimi.ingsw2022.eriantys.messages.PingMessage;
import it.polimi.ingsw2022.eriantys.messages.TimedMessage;
import it.polimi.ingsw2022.eriantys.messages.changes.Update;
import it.polimi.ingsw2022.eriantys.messages.toClient.*;
import it.polimi.ingsw2022.eriantys.messages.toServer.GameSettings;
import it.polimi.ingsw2022.eriantys.messages.toServer.PerformedMoveMessage;
import it.polimi.ingsw2022.eriantys.server.controller.BasicGameController;
import it.polimi.ingsw2022.eriantys.server.controller.ExpertGameController;
import it.polimi.ingsw2022.eriantys.server.controller.GameController;
import it.polimi.ingsw2022.eriantys.server.controller.GameMode;
import it.polimi.ingsw2022.eriantys.server.model.Game;
import it.polimi.ingsw2022.eriantys.server.model.players.Player;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class represents an instance of a server. It's used to start and manage the connection with clients, to set
 * the game settings, and to send and receive messages.
 *
 * @author Niccolò Nicolosi
 * @author Francesco Melegati Maccari
 * @author Emanuele Musto
 */
public class EriantysServer implements Serializable {
    
    public static final int PORT_NUMBER = 65000;
    public static final int MAX_USERNAME_LENGTH = 20;
    public static final String SAVE_FILE_PATH = "save";
    
    private static EriantysServer instance;
    
    public static void launch(String[] args) throws IOException, InterruptedException {
        
        createInstance();
        getInstance().start();
    }
    
    public static EriantysServer getInstance() {
        
        return instance;
    }
    
    /**
     * Creates a single instance of the server.
     */
    private static void createInstance() throws IOException {
        
        instance = new EriantysServer();
    }
    
    private transient final ServerSocket serverSocket;
    private transient final Map<String, Socket> clients;
    private transient final Map<Socket, ObjectOutputStream> clientOutputStreams;
    private transient final Map<Socket, ObjectInputStream> clientInputStreams;
    private transient final List<Timer> clientPingTimers;
    private transient final Map<Integer, AtomicBoolean> messageLocks;
    private transient Socket currentlyConnectingClient;
    private int nextLockId;
    private GameSettings gameSettings;
    private Game game;
    private GameController gameController;
    private boolean running;
    private EriantysServer loadedSave;
    
    private EriantysServer() throws IOException {
        
        //open server
        serverSocket = new ServerSocket(PORT_NUMBER);
        
        //initialize client maps
        clients = new LinkedHashMap<>(4);
        clientOutputStreams = new HashMap<>(4);
        clientInputStreams = new HashMap<>(4);
        clientPingTimers = new ArrayList<>(4);
        messageLocks = new HashMap<>(10);
        
        nextLockId = 0;
        
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            
            e.printStackTrace();
            if (running) shutdown(false);
        });
    }
    
    /**
     * Sends a message to the client knowing only the socket.
     *
     * @param message the message to send.
     * @param client  the socket of the client that will receive the message.
     * @throws IOException when output stream throws an exception (while sending a message to client).
     */
    public void sendToClient(Message message, Socket client) throws IOException {
        
        sendToClient(message, client, null);
    }
    
    /**
     * Sends a message to the client knowing only the player's username.
     *
     * @param message        the message to send.
     * @param clientUsername the player's username associated to the client.
     * @throws IOException when output stream throws an exception (while sending a message to client).
     */
    public void sendToClient(Message message, String clientUsername) throws IOException {
        
        sendToClient(message, clients.get(clientUsername), clientUsername);
    }
    
    /**
     * Sends a message to every client.
     *
     * @param message the message to send.
     */
    public void sendToAllClients(Message message) {
        
        for (String username : clients.keySet()) {
            
            try {
                
                sendToClient(message, clients.get(username), username);
            }
            catch (IOException e) {
                
                e.printStackTrace();
            }
        }
    }
    
    public Socket getCurrentlyConnectingClient() {
        
        return currentlyConnectingClient;
    }
    
    /**
     * @param username the chosen username.
     * @return false if the username is already used by someone.
     */
    public boolean isAvailableUsername(String username) {
        
        return !clients.containsKey(username);
    }
    
    /**
     * Saves the socket and username in the server.
     *
     * @param clientSocket the socket of the client.
     * @param username     the username of the player.
     */
    public synchronized void addClient(Socket clientSocket, String username) {
        
        if (clients.containsValue(clientSocket)) throw new RuntimeException("Client already connected");
        
        clients.put(username, clientSocket);
        System.out.println("Added client with username: " + username);
        currentlyConnectingClient = null;
        
        notifyAll();
    }
    
    /**
     * Saves the game settings chosen by the first player connecting to the server.
     *
     * @param gameSettings the chosen game settings.
     */
    public synchronized void addGameSettings(GameSettings gameSettings) {
        
        this.gameSettings = gameSettings;
        notifyAll();
    }
    
    /**
     * Calls the controller whenever a message containing a move from the client arrives.
     *
     * @param moveMessage the move message arrived from one client.
     * @throws IOException when output stream throws an exception (while sending a message to client).
     */
    public void managePerformedMoveMessage(PerformedMoveMessage moveMessage) throws IOException {
        
        try {
            
            gameController.managePerformedMoveMessage(moveMessage);
        }
        catch (InterruptedException e) {
            
            e.printStackTrace();
        }
    }
    
    /**
     * Gets (or creates if not present) a lockId used for synchronization by the applicant.
     *
     * @param lockId the requested lockId.
     * @return the lock associated with the lockId.
     */
    public AtomicBoolean getMessageLock(int lockId) {
        
        return messageLocks.computeIfAbsent(lockId, k -> new AtomicBoolean());
    }
    
    /**
     * @return the next available lock id.
     */
    public int getNextMessageLockId() {
        
        nextLockId += 1;
        return nextLockId;
    }
    
    /**
     * Removes a lock when the user doesn't need it anymore.
     *
     * @param lockId the lock id to remove.
     */
    public void removeMessageLock(int lockId) {
        
        messageLocks.remove(lockId);
    }
    
    /**
     * Saves in a file the current game, the current state of the controller, and every information when a client disconnects
     * and the game stops. The game can then be resumed later, when someone connects again.
     *
     * @throws IOException when output stream throws an exception.
     */
    public void saveGame() throws IOException {
        
        File saveFile = new File(SAVE_FILE_PATH);
        saveFile.createNewFile();
        FileOutputStream outputStream = new FileOutputStream(saveFile);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        
        System.out.println("Saving the game");
        try {
            
            objectOutputStream.writeObject(this);
        }
        catch (IOException e) {
            
            e.printStackTrace();
            System.out.println("Could not save the game");
            return;
        }
        System.out.println("Game saved");
        
        objectOutputStream.close();
    }
    
    /**
     * Disconnects every client, interrupt every thread, closes sockets and server.
     *
     * @param playerDisconnected true if the shutdown is caused by a player disconnecting.
     */
    public void shutdown(boolean playerDisconnected) {
        
        if (running) {
            
            System.out.println("Shutting down");
            running = false;
            
            if (playerDisconnected)
                sendToAllClients(new ErrorMessage("A player disconnected from the server\nServer shutdown: disconnected"));
            else sendToAllClients(new ErrorMessage("Internal server error\nServer shutdown: disconnected"));
            
            for (Timer timer : clientPingTimers) {
                
                timer.cancel();
                timer.purge();
            }
            
            for (Socket socket : clients.values()) {
                
                try {
                    
                    socket.close();
                }
                catch (IOException e) {
                    
                    e.printStackTrace();
                }
            }
            
            Set<Thread> runningThreads = Thread.getAllStackTraces().keySet();
            for (Thread thread : runningThreads) thread.interrupt();
        }
    }
    
    /**
     * Starts the server and allow for clients to connect to it.
     * Requests the game settings and username to the first client to connect. To the other clients only the username,
     * and lets them wait in a lobby.
     *
     * @throws IOException          when output stream throws an exception (while sending a message to client).
     * @throws InterruptedException when the thread is interrupted.
     */
    private void start() throws IOException, InterruptedException {
        
        running = true;
        System.out.println("\nServer started\nWaiting for the first player to join...");
        
        //accept the first connection
        acceptConnection();
        String firstClient;
        
        synchronized (this) {
            
            while (clients.size() == 0) wait();
            firstClient = (String) clients.keySet().toArray()[0];
            startPing(firstClient);
            notifyAll();
        }
        
        requestGameSettings(firstClient);
        
        synchronized (this) {
            
            while (gameSettings == null) wait();
            
            if (gameSettings.loadGame) {
                
                loadSave();
                if (loadedSave != null) gameSettings = loadedSave.gameSettings;
            }
            
            sendToClient(new GameJoinedMessage(clients.keySet().toArray(new String[0]), gameSettings), firstClient);
            notifyAll();
        }
        
        //accept other connections
        for (int n = 1; n < gameSettings.numberOfPlayers; n++) {
            
            acceptConnection();
            String currentClient;
            
            synchronized (this) {
                
                while (clients.size() <= n) wait();
                
                String[] playerUsernames = clients.keySet().toArray(new String[0]);
                currentClient = playerUsernames[n];
                sendToClient(new GameJoinedMessage(playerUsernames, gameSettings), currentClient);
                startPing(currentClient);
                
                for (String client : clients.keySet())
                    if (!client.equals(currentClient))
                        sendToClient(new NewPlayerJoinedMessage(playerUsernames), client);
                
                notifyAll();
            }
        }
        
        if (loadedSave == null) createGame();   //initialize new game
        else loadGame();                       //load game
        
        //Start controller
        gameController.playGame();
        
        System.out.println("Game ended: deleting save file");
        File saveFile = new File("save");
        saveFile.delete();
    }
    
    /**
     * Accepts the connection of one client, initializes the input and output streams, notifies the client of the
     * successful connection, and sends a message asking for the username.
     *
     * @throws IOException          when output stream throws an exception (while sending a message to client).
     * @throws InterruptedException when the thread waiting for a response by the client is interrupted.
     */
    private void acceptConnection() throws IOException, InterruptedException {
        
        //accept a connection
        Socket client = serverSocket.accept();
        currentlyConnectingClient = client;
        System.out.println("Connection established with client: " + client);
        
        //initialize object the streams of the accepted client
        clientOutputStreams.put(client, new ObjectOutputStream(client.getOutputStream()));
        clientInputStreams.put(client, new ObjectInputStream(client.getInputStream()));
        
        //notify the client of the successful connection
        sendToClient(new ConnectedMessage(), client);
        
        listenToClient(client);
        
        //ask the connected client to provide a username
        TimedMessage sent = new ChooseUsernameMessage();
        sendToClient(sent, client);
        sent.waitForValidResponse();
    }
    
    /**
     * Sends a request message for the game settings (number of players and game mode).
     *
     * @param clientUsername the username of the player that will receive the request.
     * @throws InterruptedException when the thread waiting for a response by the client is interrupted.
     * @throws IOException          when output stream throws an exception (while sending a message to client).
     */
    private void requestGameSettings(String clientUsername) throws InterruptedException, IOException {
        
        ChooseGameSettingsMessage sent = new ChooseGameSettingsMessage();
        sendToClient(sent, clientUsername);
        sent.waitForValidResponse();
    }
    
    /**
     * Creates a thread for every client that connects to the server.
     * In the thread, while the server is running, it listens to clients and receives every message sent by them.
     * When a message is received, the server manage and reply accordingly based on the message received.
     *
     * @param client the socket of the client successfully connected to the server.
     */
    private void listenToClient(Socket client) {
        
        new Thread(() -> {
            
            ObjectInputStream clientInputStream = clientInputStreams.get(client);
            String clientUsername = null;
            while (running) {
                
                try {
                    
                    Optional<Message> message = readMessageFromClient(clientInputStream);
                    if (message.isPresent()) {
                        
                        System.out.println("Message received from " + (clientUsername != null ? clientUsername : client) + ": " + message.get().getClass().getSimpleName());
                        message.get().manageAndReply();
                    }
                    
                    if (clientUsername == null) for (String username : clients.keySet())
                        if (clients.get(username) == client) {
                            
                            clientUsername = username;
                            break;
                        }
                }
                catch (SocketException e) {
                    
                    System.out.println("Could not listen to a closed socket: " + client + (clientUsername != null ? (" (" + clientUsername + ")") : ""));
                    if (running) shutdown(true);
                }
                catch (IOException | ClassNotFoundException e) {
                    
                    e.printStackTrace();
                    if (running) shutdown(true);
                }
            }
        }).start();
    }
    
    /**
     * Reads from the input stream of one client.
     *
     * @param clientInputStream the input stream to read from.
     * @return an optional containing a message if present, or nothing otherwise.
     * @throws IOException            when input stream throws an exception (while reading a message from client).
     * @throws ClassNotFoundException when the object read from the stream is not of a java class of the program.
     */
    private Optional<Message> readMessageFromClient(ObjectInputStream clientInputStream) throws IOException, ClassNotFoundException {
        
        try {
            
            Message message = (Message) clientInputStream.readObject();
            return Optional.of(message);
        }
        catch (EOFException e) {
            
            return Optional.empty();
        }
    }
    
    /**
     * Sends a message to one client.
     *
     * @param message        the message to send.
     * @param client         the socket of the client that will receive the message.
     * @param clientUsername the player's username associated to the client.
     * @throws IOException when output stream throws an exception (while sending a message to client).
     */
    private void sendToClient(Message message, Socket client, String clientUsername) throws IOException {
        
        if (client.isClosed()) return;
        
        try {
            
            ObjectOutputStream outputStream = clientOutputStreams.get(client);
            
            synchronized (outputStream) {
                
                outputStream.reset();
                outputStream.writeObject(message);
                outputStream.notifyAll();
            }
            
            System.out.println("Message sent to " + (clientUsername != null ? clientUsername : client) + ": " + message.getClass().getSimpleName());
        }
        catch (SocketException e) {
            
            System.out.println(client + (clientUsername != null ? (" (" + clientUsername + ")") : "") + " closed: couldn't send " + message.getClass().getSimpleName());
            if (running) shutdown(true);
        }
    }
    
    /**
     * Starts a series of ping messages (one every 15 seconds) to check for client disconnections.
     *
     * @param clientUsername the player's username to send the pings to.
     */
    private void startPing(String clientUsername) {
        
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            
            @Override
            public void run() {
                
                try {
                    
                    PingMessage sent = new PingMessage();
                    sendToClient(sent, clientUsername);
                    sent.waitForValidResponse();
                }
                catch (IOException | InterruptedException e) {
                    
                    e.printStackTrace();
                }
            }
        }, 0, 15000);
        clientPingTimers.add(timer);
    }
    
    /**
     * Creates an instance of the game and one of the controller (basic or expert game mode) when every player needed is connected.
     * It also sends the initial update to everyone.
     *
     * @throws IOException when output stream throws an exception (while sending a message to client).
     * @see Game
     * @see BasicGameController
     * @see ExpertGameController
     */
    private void createGame() throws IOException {
        
        System.out.println("Creating a new game");
        
        String[] playerUsernames = clients.keySet().toArray(new String[0]);
        
        game = new Game(playerUsernames);
        gameController = gameSettings.gameMode == GameMode.BASIC ? new BasicGameController(game) : new ExpertGameController(game, true);
        
        Update[] initialUpdates = gameController.createInitialUpdates();
        
        System.out.println("Sending initial update");
        
        for (int n = 0; n < playerUsernames.length; n++)
            sendToClient(new StartingGameMessage(game.getPlayersStartOrder(), gameSettings.gameMode, initialUpdates[n]), clients.get(playerUsernames[n]));
    }
    
    /**
     * Loads a game previously saved when the first player decides to resume the last game.
     *
     * @throws IOException when output stream throws an exception (while sending a message to client).
     */
    private void loadGame() throws IOException {
        
        System.out.println("Loading the game");
        
        game = loadedSave.game;
        
        List<Player> players = game.getPlayersStartOrder();
        List<String> playerUsernames = new ArrayList<>(clients.keySet());
        
        for (int n = 0; n < playerUsernames.size(); n++) players.get(n).setUsername(playerUsernames.get(n));
        
        gameController = loadedSave.gameSettings.gameMode == GameMode.BASIC ? new BasicGameController(game) : new ExpertGameController(game, false);
        gameController.setCurrentGamePhase(loadedSave.gameController.getCurrentGamePhase());
        gameController.setEndGameNow(loadedSave.gameController.getEndGameNow());
        
        Update[] initialUpdates = gameController.createInitialUpdates();
        
        for (int n = 0; n < playerUsernames.size(); n++)
            sendToClient(new StartingGameMessage(game.getPlayersStartOrder(), gameSettings.gameMode, initialUpdates[n]), clients.get(playerUsernames.get(n)));
        System.out.println("Sending initial update");
    }
    
    /**
     * Retrieves from a file the previously saved game.
     *
     * @throws IOException when output stream throws an exception.
     */
    private void loadSave() throws IOException {
        
        System.out.println("Loading a game save");
        
        FileInputStream inputStream;
        try {
            
            inputStream = new FileInputStream(SAVE_FILE_PATH);
        }
        catch (FileNotFoundException e) {
            
            e.printStackTrace();
            System.out.println("No save file found");
            return;
        }
        
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        
        try {
            
            loadedSave = (EriantysServer) objectInputStream.readObject();
        }
        catch (ClassNotFoundException e) {
            
            System.out.println("Wrong save format");
        }
        System.out.println("Game loaded");
        
        objectInputStream.close();
    }
}
