package it.polimi.ingsw2022.eriantys.server;

import it.polimi.ingsw2022.eriantys.messages.toClient.*;
import it.polimi.ingsw2022.eriantys.messages.Message;
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

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

    private Socket currentlyConnectingClient;
    private final Map<String, Socket> clients;
    private int nextLockId;
    private final Map<Socket, ObjectOutputStream> clientOutputStreams;
    private final Map<Socket, ObjectInputStream> clientInputStreams;
    private final List<Thread> clientThreads;
    private final Map<Integer, AtomicBoolean> messageLocks;

    private GameSettings gameSettings;
    private Game game;
    private GameMode gameMode;
    private boolean running;

    private EriantysServer() throws IOException {

        //open server
        serverSocket = new ServerSocket(PORT_NUMBER);

        //initialize client maps
        clients = new LinkedHashMap<>(4);
        messageLocks = new HashMap<>(10);
        clientOutputStreams = new HashMap<>(4);
        clientInputStreams = new HashMap<>(4);
        clientThreads = new ArrayList<>(4);
    }

    private void start() throws IOException, InterruptedException {

        running = true;
        System.out.println("\nServer started\nWaiting for the first player to join...");

        //accept first connection
        Socket firstClient = acceptConnection();
        requestGameSettings(firstClient);

        synchronized (this) {

            while (gameSettings == null) wait();
            sendToClient(new GameJoinedMessage(clients.keySet().toArray(new String[0]), gameSettings), firstClient);
            startPing(firstClient);
            notifyAll();
        }

        //accept other connections
        for (int n = 1; n < gameSettings.numberOfPlayers; n++) {

            Socket currentClient = acceptConnection();

            synchronized (clients) {

                while (clients.size() <= n) clients.wait();

                String[] playerUsernames = clients.keySet().toArray(new String[0]);
                sendToClient(new GameJoinedMessage(playerUsernames, gameSettings), currentClient);

                for (Socket client : clients.values())
                    if (client != currentClient)
                        sendToClient(new NewPlayerJoinedMessage(playerUsernames), client);

                startPing(currentClient);
                clients.notifyAll();
            }
        }

        //initialize game
        createGame();
        //Start controller
        gameMode.playGame();
    }

    private Socket acceptConnection() throws IOException, InterruptedException {

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

        return client;
    }

    private void requestGameSettings(Socket client) throws InterruptedException, IOException {

        synchronized(clients) {

            while(clients.size() == 0) clients.wait();
            ChooseGameSettingsMessage sent = new ChooseGameSettingsMessage();
            sendToClient(sent, client);
            //sent.waitForValidResponse();

            clients.notifyAll();
        }
    }

    private void listenToClient(Socket client) {

        Thread thread = new Thread(() -> {

            ObjectInputStream clientInputStream = clientInputStreams.get(client);
            while(running) {

                try {

                    Optional<ToServerMessage> message = readMessageFromClient(clientInputStream);
                    if(message.isPresent()) {

                        System.out.println("Message received: " + message.get().getClass().getSimpleName());
                        message.get().manageAndReply();
                    }
                }
                catch(SocketException e) {

                    running = false;
                    System.out.println(client + " closed while listening");
                }
                catch(IOException | ClassNotFoundException e) {

                    running = false;
                    e.printStackTrace();
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

    public void sendToClient(Message message, Socket client) throws IOException {

        ObjectOutputStream outputStream = clientOutputStreams.get(client);

        synchronized(outputStream) {

            outputStream.reset();
            outputStream.writeObject(message);
            outputStream.notify();
        }

        System.out.println("Message sent: " + message.getClass().getSimpleName());
    }

    public void sendToClient(Message message, String clientUsername) throws IOException {

        sendToClient(message, clients.get(clientUsername));
    }

    public void sendToAllClients(Message message) throws IOException {

        for(Socket client : clients.values()) sendToClient(message, client);
    }

    public Socket getCurrentlyConnectingClient() {

        return currentlyConnectingClient;
    }

    public boolean isAvailableUsername(String username) {

        return !clients.containsKey(username);
    }

    public void addClient(Socket clientSocket, String username) {

        if(clients.containsValue(clientSocket)) throw new RuntimeException("Client already connected");

        synchronized(clients) {

            clients.put(username, clientSocket);
            System.out.println("Added client with username: " + username);
            currentlyConnectingClient = null;

            clients.notify();
        }
    }

    public synchronized void addGameSettings(GameSettings gameSettings) {

        this.gameSettings = gameSettings;
        notify();
    }

    private void startPing(Socket client){

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    PingMessage sent = new PingMessage();
                    sendToClient(sent, client);
                    sent.waitForValidResponse();

                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 10000);
    }

    /**
     * @return an array of initial updates, one for each player
     */
    private Update[] createInitialUpdates() {

        Player[] players = game.getPlayers();
        Update[] initUpdates = new Update[players.length];

        CharacterCardsChange characterCardsChange = new CharacterCardsChange();
        for(int n = 0; n < game.getNumberOfCharacters(); n++) characterCardsChange.addCharacterCard(game.getCharacter(n));

        IslandChange[] islandChanges = new IslandChange[game.getBoard().getNumberOfIslands()];
        for (int n = 0; n < islandChanges.length; n++) islandChanges[n] = new IslandChange(n, game.getBoard().getIsland(n));

        CloudChange[] cloudChanges = new CloudChange[players.length];
        for (int n = 0; n < cloudChanges.length; n++) cloudChanges[n] = new CloudChange(n, game.getBoard().getCloud(n));

        SchoolChange[] schoolChanges = new SchoolChange[players.length];
        for (int n = 0; n < players.length; n++) schoolChanges[n] = new SchoolChange(players[n].getSchool());

        PlayerChange[] playerChanges = new PlayerChange[players.length];
        for (int n = 0; n < players.length; n++) playerChanges[n] = new PlayerChange(players[n]);

        for (int n = 0; n < initUpdates.length; n++) {

            initUpdates[n] = new Update();
            initUpdates[n].addChange(characterCardsChange);
            for (IslandChange islandChange : islandChanges) initUpdates[n].addChange(islandChange);
            for (CloudChange cloudChange : cloudChanges) initUpdates[n].addChange(cloudChange);
            for (SchoolChange schoolChange : schoolChanges) initUpdates[n].addChange(schoolChange);
            for (PlayerChange playerChange : playerChanges) initUpdates[n].addChange(playerChange);
        }

        for (int n = 0; n < players.length; n++) {

            Player player = players[n];

            HelperCardsChange helperCardsChange = new HelperCardsChange();
            for(int i = 0; i < player.getNumberOfHelpers(); i++) helperCardsChange.addHelperCard(player.getHelperCard(i));
            initUpdates[n].addChange(helperCardsChange);
        }

        return initUpdates;
    }

    private void createGame() throws IOException {

        String[] playerUsernames = clients.keySet().toArray(new String[0]);

        game = new Game(playerUsernames);
        gameMode = gameSettings.gameMode == Mode.BASIC ? new BasicGameMode(game) : new ExpertGameMode(game);

        Update[] initialUpdates = createInitialUpdates();


        for (int n = 0; n < playerUsernames.length; n++)
            sendToClient(new StartingGameMessage(game.getPlayers(), gameSettings.gameMode, initialUpdates[n]), clients.get(playerUsernames[n]));
        System.out.println("Sent initial update");
    }

    public void setPerformedMoveMessage(PerformedMoveMessage moveMessage) {

        gameMode.setPerformedMoveMessage(moveMessage);
    }

    public AtomicBoolean getLock(int lockId){

        return messageLocks.computeIfAbsent(lockId, k -> new AtomicBoolean());
    }

    public int getNextLockId(){
        nextLockId += 1;
        return nextLockId;
    }

    public void removeLock(int lockId){
        messageLocks.remove(lockId);
    }

    public void shutdown() throws IOException {

        System.out.println("Shutting down");

        running = false;
        for (Thread thread : clientThreads) thread.interrupt();
        for (Socket socket : clients.values()) socket.close();
        for (ObjectOutputStream outputStream : clientOutputStreams.values()) outputStream.close();
        for (ObjectInputStream inputStream : clientInputStreams.values()) inputStream.close();
    }
}
