package it.polimi.ingsw2022.eriantys.server;

import it.polimi.ingsw2022.eriantys.messages.Move.Move;
import it.polimi.ingsw2022.eriantys.messages.toClient.*;
import it.polimi.ingsw2022.eriantys.messages.Message;
import it.polimi.ingsw2022.eriantys.messages.toClient.changes.GameInitChange;
import it.polimi.ingsw2022.eriantys.messages.toClient.changes.IslandChange;
import it.polimi.ingsw2022.eriantys.messages.toClient.changes.Update;
import it.polimi.ingsw2022.eriantys.messages.toServer.GameSettings;
import it.polimi.ingsw2022.eriantys.messages.toServer.ToServerMessage;
import it.polimi.ingsw2022.eriantys.server.model.Game;
import it.polimi.ingsw2022.eriantys.server.model.cards.HelperCard;
import it.polimi.ingsw2022.eriantys.server.model.players.Player;
import it.polimi.ingsw2022.eriantys.server.model.players.Team;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
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

    private Update[] createInitialUpdate() {

        Update[] initUpdates = new Update[gameSettings.numberOfPlayers];
        for(int n = 0; n < initUpdates.length; n++) initUpdates[n] = new Update();

        //add a game init change to every initUpdate (a different one for every player)
        Player[] players = game.getPlayers();
        for(int n = 0; n < players.length; n++) {

            GameInitChange gameInitChange = new GameInitChange();

            Player player = players[n];
            for(int i = 0; i < player.getNumberOfHelpers(); i++)
                gameInitChange.addHelperCard(player.getHelperCard(i));

            for(int i = 0; i < game.getNumberOfCharacters(); i++)
                gameInitChange.addCharacterCard(game.getCharacter(i));

            initUpdates[n].addChange(gameInitChange);
        }

        //add island changes to every initUpdate
        for (int n = 0; n < game.getBoard().numberOfIslands(); n++) {

            IslandChange change = new IslandChange(n, game.getBoard().getIsland(n));
            for(Update initUpdate : initUpdates) initUpdate.addChange(change);
        }

        return initUpdates;
    }

    private void createGame() throws IOException {

        String[] playerUsernames = clients.keySet().toArray(new String[0]);

        game = new Game(playerUsernames, gameSettings.gameMode);

        Player[] players = game.getPlayers();
        Team[] playerTeams = new Team[players.length];
        for(int n = 0; n < players.length; n++) playerTeams[n] = players[n].team;

        Update[] initialUpdates = createInitialUpdate();

        for (int n = 0; n < playerUsernames.length; n++) {

            sendToClient(new StartingGameMessage(playerUsernames, playerTeams, gameSettings.gameMode, initialUpdates[n]),
                    clients.get(playerUsernames[n]));
        }
    }

    public void setPerformedMove(Move move, String username){
        //set sul controller della move ( avrà syncronized e notify alla fine)
    }
}
