package it.polimi.ingsw2022.eriantys.server;

import it.polimi.ingsw2022.eriantys.messages.Move.Move;
import it.polimi.ingsw2022.eriantys.messages.toClient.*;
import it.polimi.ingsw2022.eriantys.messages.Message;
import it.polimi.ingsw2022.eriantys.messages.toClient.changes.*;
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
    private Game game;
    private GameMode gameMode;
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

        //accept first connection
        Socket firstClient = acceptConnection();
        requestGameSettings(firstClient);

        synchronized (this) {

            while (gameSettings == null) wait();
            sendToClient(new GameJoinedMessage(clients.keySet().toArray(new String[0]), gameSettings), firstClient);
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

                clients.notifyAll();
            }
        }

        //initialize game
        createGame();
        //Start controller
        gameMode.playGame();
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

                    running = false;
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void sendToClient(Message message, Socket clientSocket) throws IOException {

        ObjectOutputStream outputStream = clientOutputStreams.get(clientSocket);
        synchronized(outputStream) {
            outputStream.writeObject(message);
        }

        System.out.println("Message sent: " + message.getClass().getSimpleName());
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

    /**
     * @return an array of initial updates, one for each player
     */
    private Update[] createInitialUpdates() {

        Player[] players = game.getPlayers();
        Update[] initUpdates = new Update[players.length];

        CharacterCardsChange characterCardsChange = new CharacterCardsChange();
        for(int n = 0; n < game.getNumberOfCharacters(); n++) characterCardsChange.addCharacterCard(game.getCharacter(n));

        IslandChange[] islandChanges = new IslandChange[game.getBoard().getNumberOfIslands()];
        for (int n = 0; n < islandChanges.length; n++) islandChanges[n] = new IslandChange(n + 1, game.getBoard().getIsland(n));

        CloudChange[] cloudChanges = new CloudChange[players.length];
        for (int n = 0; n < players.length; n++) cloudChanges[n] = new CloudChange(n + 1, game.getBoard().getCloud(n));

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

    public Socket getClientSocket(String username) {
        return clients.get(username);
    }

    public void setPerformedMoveMessage(PerformedMoveMessage moveMessage) {
        // set sul controller della move ( avrà synchronized e notify alla fine)
        gameMode.setPerformedMoveMessage(moveMessage);

    }
}
