package it.polimi.ingsw2022.eriantys.server;

import it.polimi.ingsw2022.eriantys.messages.toClient.*;
import it.polimi.ingsw2022.eriantys.messages.changes.*;
import it.polimi.ingsw2022.eriantys.messages.toServer.GameSettings;
import it.polimi.ingsw2022.eriantys.messages.toServer.PerformedMoveMessage;
import it.polimi.ingsw2022.eriantys.messages.toServer.ToServerMessage;
import it.polimi.ingsw2022.eriantys.server.controller.BasicGameMode;
import it.polimi.ingsw2022.eriantys.server.controller.ExpertGameMode;
import it.polimi.ingsw2022.eriantys.server.controller.GameMode;
import it.polimi.ingsw2022.eriantys.server.controller.Mode;
import it.polimi.ingsw2022.eriantys.server.model.Game;
import it.polimi.ingsw2022.eriantys.server.model.players.Player;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Niccolò Nicolosi
 * @author Francesco Melegati Maccari
 * @author Emanuele Musto
 */
public class EriantysServer implements Serializable {

    public static final int PORT_NUMBER = 65000;
    public static final int MAX_USERNAME_LENGTH = 20;
    public static final String SAVE_FILE_PATH = "save";

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

    private transient final ServerSocket serverSocket;

    private transient Socket currentlyConnectingClient;
    private transient final Map<String, Socket> clients;
    private transient final Map<Socket, ObjectOutputStream> clientOutputStreams;
    private transient final Map<Socket, ObjectInputStream> clientInputStreams;
    private transient final List<Thread> clientThreads;
    private transient final List<Timer> clientPingTimers;
    private transient final Map<Integer, AtomicBoolean> messageLocks;

    private int nextLockId;

    private GameSettings gameSettings;
    private Game game;
    private GameMode gameMode;
    private boolean running;

    private EriantysServer loadedSave;

    private EriantysServer() throws IOException {

        //open server
        serverSocket = new ServerSocket(PORT_NUMBER);

        //initialize client maps
        clients = new LinkedHashMap<>(4);
        clientOutputStreams = new HashMap<>(4);
        clientInputStreams = new HashMap<>(4);
        clientThreads = new ArrayList<>(4);
        clientPingTimers = new ArrayList<>(4);
        messageLocks = new HashMap<>(10);

        nextLockId = 0;
    }

    private void start() throws IOException, InterruptedException {

        running = true;
        System.out.println("\nServer started\nWaiting for the first player to join...");

        //accept the first connection
        acceptConnection();
        String firstClient;

        synchronized(this) {

            while(clients.size() == 0) wait();
            firstClient = (String) clients.keySet().toArray()[0];
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
            startPing(firstClient);
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

                for (String client : clients.keySet())
                    if (!client.equals(currentClient))
                        sendToClient(new NewPlayerJoinedMessage(playerUsernames), client);

                startPing(currentClient);
                notifyAll();
            }
        }

        if (loadedSave == null) createGame();   //initialize new game
        else loadGame();                        //load game

        //Start controller
        gameMode.playGame();

        System.out.println("Game ended: deleting save file");
        File saveFile = new File("save");
        saveFile.delete();
    }

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

    private void requestGameSettings(String clientUsername) throws InterruptedException, IOException {

        ChooseGameSettingsMessage sent = new ChooseGameSettingsMessage();
        sendToClient(sent, clientUsername);
        sent.waitForValidResponse();
    }

    private void listenToClient(Socket client) {

        Thread thread = new Thread(() -> {

            ObjectInputStream clientInputStream = clientInputStreams.get(client);
            String clientUsername = null;
            while(running) {

                try {

                    Optional<ToServerMessage> message = readMessageFromClient(clientInputStream);
                    if(message.isPresent()) {

                        System.out.println("Message received from " + (clientUsername != null ? clientUsername : client) +
                                           ": " + message.get().getClass().getSimpleName());
                        message.get().manageAndReply();
                    }

                    if(clientUsername == null)
                        for(String username : clients.keySet()) if(clients.get(username) == client) {

                            clientUsername = username;
                            break;
                        }
                }
                catch(SocketException e) {

                    System.out.println("Could not listen to a closed socket: " + client +
                                       (clientUsername != null ? (" (" + clientUsername + ")") : ""));

                    try { if(running) shutdown(true); }
                    catch(IOException ex) {

                        running = false;
                        ex.printStackTrace();
                    }
                }
                catch(IOException | ClassNotFoundException e) {

                    e.printStackTrace();

                    try { if(running) shutdown(true); }
                    catch(IOException ex) {

                        running = false;
                        ex.printStackTrace();
                    }
                }
            }
        });

        clientThreads.add(thread);
        thread.start();
    }

    private Optional<ToServerMessage> readMessageFromClient(ObjectInputStream clientInputStream) throws IOException, ClassNotFoundException {

        try {

            ToServerMessage message = (ToServerMessage) clientInputStream.readObject();
            return Optional.of(message);
        }
        catch(EOFException e) {

            return Optional.empty();
        }
    }

    private void sendToClient(ToClientMessage message, Socket client, String clientUsername) throws IOException {

        if(client.isClosed()) return;

        try {

            ObjectOutputStream outputStream = clientOutputStreams.get(client);

            synchronized(outputStream) {

                outputStream.reset();
                outputStream.writeObject(message);
                outputStream.notifyAll();
            }

            System.out.println("Message sent to " + (clientUsername != null ? clientUsername : client) +
                               ": " + message.getClass().getSimpleName());
        }
        catch(SocketException e) {

            System.out.println(client + (clientUsername != null ? (" (" + clientUsername + ")") : "") +
                               " closed: couldn't send " + message.getClass().getSimpleName());
            if(running) shutdown(true);
        }
    }

    public void sendToClient(ToClientMessage message, Socket client) throws IOException {

        sendToClient(message, client, null);
    }

    public void sendToClient(ToClientMessage message, String clientUsername) throws IOException {

        sendToClient(message, clients.get(clientUsername), clientUsername);
    }

    public void sendToAllClients(ToClientMessage message) throws IOException {

        for(String username : clients.keySet()) sendToClient(message, clients.get(username), username);
    }

    public Socket getCurrentlyConnectingClient() {

        return currentlyConnectingClient;
    }

    public boolean isAvailableUsername(String username) {

        return !clients.containsKey(username);
    }

    public synchronized void addClient(Socket clientSocket, String username) {

        if(clients.containsValue(clientSocket)) throw new RuntimeException("Client already connected");

        clients.put(username, clientSocket);
        System.out.println("Added client with username: " + username);
        currentlyConnectingClient = null;

        notifyAll();
    }

    public synchronized void addGameSettings(GameSettings gameSettings) {

        this.gameSettings = gameSettings;
        notifyAll();
    }

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

    private void createGame() throws IOException {

        System.out.println("Creating a new game");

        String[] playerUsernames = clients.keySet().toArray(new String[0]);

        game = new Game(playerUsernames);
        gameMode = gameSettings.gameMode == Mode.BASIC ? new BasicGameMode(game) : new ExpertGameMode(game, true);

        Update[] initialUpdates = gameMode.createInitialUpdates();

        for (int n = 0; n < playerUsernames.length; n++)
            sendToClient(new StartingGameMessage(game.getPlayersStartOrder(), gameSettings.gameMode, initialUpdates[n]), clients.get(playerUsernames[n]));
        System.out.println("Sending initial update");
    }

    private void loadGame() throws IOException {

        System.out.println("Loading the game");

        game = loadedSave.game;

        List<Player> players = game.getPlayersStartOrder();
        List<String> playerUsernames = new ArrayList<>(clients.keySet());

        for(int n = 0; n < playerUsernames.size(); n++) players.get(n).setUsername(playerUsernames.get(n));

        gameMode = loadedSave.gameSettings.gameMode == Mode.BASIC ? new BasicGameMode(game) : new ExpertGameMode(game, false);
        gameMode.setCurrentGamePhase(loadedSave.gameMode.getCurrentGamePhase());
        gameMode.setEndGameNow(loadedSave.gameMode.getEndGameNow());

        Update[] initialUpdates = gameMode.createInitialUpdates();

        for (int n = 0; n < playerUsernames.size(); n++)
            sendToClient(new StartingGameMessage(game.getPlayersStartOrder(), gameSettings.gameMode, initialUpdates[n]), clients.get(playerUsernames.get(n)));
        System.out.println("Sending initial update");
    }

    public void managePerformedMoveMessage(PerformedMoveMessage moveMessage) throws IOException {

        try{

            gameMode.managePerformedMoveMessage(moveMessage);
        }
        catch(InterruptedException e) {

            e.printStackTrace();
        }
    }

    public AtomicBoolean getMessageLock(int lockId) {

        return messageLocks.computeIfAbsent(lockId, k -> new AtomicBoolean());
    }

    public int getNextMessageLockId() {

        nextLockId += 1;
        return nextLockId;
    }

    public void removeMessageLock(int lockId) {

        messageLocks.remove(lockId);
    }

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

    public void shutdown(boolean playerDisconnected) throws IOException {

        System.out.println("Shutting down");

        running = false;

        if(playerDisconnected) sendToAllClients(new PlayerDisconnectedMessage());

        for (Thread thread : clientThreads) thread.interrupt();
        for(Timer timer : clientPingTimers) {

            timer.cancel();
            timer.purge();
        }
        for (Socket socket : clients.values()) socket.close();
        for (ObjectOutputStream outputStream : clientOutputStreams.values()) outputStream.close();
        for (ObjectInputStream inputStream : clientInputStreams.values()) inputStream.close();
    }
}
