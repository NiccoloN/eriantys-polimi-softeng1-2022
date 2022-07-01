package it.polimi.ingsw2022.eriantys.client;

import it.polimi.ingsw2022.eriantys.client.view.View;
import it.polimi.ingsw2022.eriantys.client.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.client.view.gui.EriantysGUI;
import it.polimi.ingsw2022.eriantys.messages.Message;
import it.polimi.ingsw2022.eriantys.messages.PingMessage;
import it.polimi.ingsw2022.eriantys.messages.changes.Update;
import it.polimi.ingsw2022.eriantys.messages.toClient.MoveRequestMessage;
import it.polimi.ingsw2022.eriantys.messages.toServer.DisconnectMessage;
import it.polimi.ingsw2022.eriantys.messages.toServer.GameSettings;
import it.polimi.ingsw2022.eriantys.server.EriantysServer;
import it.polimi.ingsw2022.eriantys.server.controller.GameMode;
import it.polimi.ingsw2022.eriantys.server.model.players.Player;
import it.polimi.ingsw2022.eriantys.server.model.players.Team;
import javafx.application.Platform;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class represents the client of the game. The client runs the view in its main thread and communicates with the server through
 * messages in a secondary thread. The client will manage and modify the view based on the communication with the server.
 * @author Niccolò Nicolosi
 * @author Francesco Melegati Maccari
 * @author Emanuele Musto
 * @see View
 * @see EriantysServer
 * @see Message
 */
public class EriantysClient {
    
    public static final String ADDRESS_FILE_NAME = "server address.txt";
    private static EriantysClient instance;
    
    /**
     * Launches the client with the given arguments
     * @param args the arguments passed to the client. If args contains "-nogui" the cli version of the view will be launched
     * @throws IOException          if an I/O exception occurs
     * @throws InterruptedException if the thread is interrupted.
     */
    public static void launch(String[] args) throws IOException, InterruptedException {
        
        boolean gui = args.length == 0 || !args[0].equals("-nogui");
        boolean showLog = (gui && args.length >= 1 && args[0].equals("-log")) || (!gui && args.length >= 2 && args[1].equals("-log"));
        createInstance(gui, showLog);
    }
    
    /**
     * @return the singleton instance
     */
    public static EriantysClient getInstance() {
        
        return instance;
    }
    
    /**
     * Initializes the singleton instance
     * @param gui     whether the gui version of the view or the cli one should be launched
     * @param showLog whether view should show logs
     * @throws IOException if could not correctly launch the view
     */
    private static void createInstance(boolean gui, boolean showLog) throws IOException, InterruptedException {
        
        instance = new EriantysClient(showLog);
        if (!gui) instance.view = EriantysCLI.launch(showLog);
        else instance.view = EriantysGUI.launch();
    }
    
    private final boolean showLog;
    private final StringWriter log;
    private final Map<Integer, AtomicBoolean> messageLocks;
    private Socket server;
    private ObjectOutputStream serverOutputStream;
    private ObjectInputStream serverInputStream;
    private String username;
    private Timer pingTimer;
    private int nextLockId;
    private boolean running;
    private View view;
    private GameSettings gameSettings;
    
    private EriantysClient(boolean showLog) throws IOException {
        
        this.showLog = showLog;
        log = new StringWriter();
        messageLocks = new HashMap<>();
        
        //create server address file if missing
        File file = new File(ADDRESS_FILE_NAME);
        if (file.createNewFile()) {
            
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            outputStreamWriter.write("localhost");
            outputStreamWriter.close();
        }
        
        running = true;
    }
    
    /**
     * Starts the connection to the server and remains listening in a separate thread. This method blocks for a maximum of 5 seconds.
     * If the client couldn't connect within 5 seconds this method returns false.
     * @param serverIP the IP address of the server
     * @return whether the connection was successful
     */
    public boolean connectToServer(String serverIP) {
        
        FutureTask<Boolean> futureConnectionTask = new FutureTask<>(() -> {
            
            try {
                
                int port = EriantysServer.PORT_NUMBER;
                log("connecting to " + serverIP + ":" + port);
                server = new Socket(serverIP, port);
                
                //initialize object streams
                serverOutputStream = new ObjectOutputStream(server.getOutputStream());
                serverInputStream = new ObjectInputStream(server.getInputStream());
                
                listenToServer();
                
                return true;
            }
            catch (IOException e) {
                
                log("Connection failed");
                return false;
            }
        });
        
        new Thread(futureConnectionTask).start();
        
        try {
            
            return futureConnectionTask.get(5, TimeUnit.SECONDS);
        }
        catch (InterruptedException | ExecutionException e) {
            
            e.printStackTrace();
            return false;
        }
        catch (TimeoutException e) {
            
            log("Connection timeout");
            return false;
        }
    }
    
    /**
     * Appends the given logText to the log
     * @param logText the text to append
     */
    public void log(String logText) {
        
        if (!(view instanceof EriantysCLI)) System.out.println(logText);
        log.append(logText).append('\n');
    }
    
    /**
     * Gets (or creates if not present) a lockId used for synchronization by the applicant.
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
     * @param lockId the lock id to remove.
     */
    public void removeMessageLock(int lockId) {
        
        messageLocks.remove(lockId);
    }
    
    /**
     * Asks the view to provide a username and send it to the server
     * @param requestMessage the message requesting the username
     */
    public void askUsername(Message requestMessage) throws IOException {
        
        view.askUsername(requestMessage);
    }
    
    /**
     * Starts ping messages from client (because this client has surely been registered to the server at this point)
     * and asks the view to provide game settings and send them to the server
     * @param requestMessage the message requesting game settings
     */
    public void askGameSettings(Message requestMessage) throws IOException {
        
        if (pingTimer == null) startPing();
        view.askGameSettings(requestMessage);
    }
    
    /**
     * Starts ping messages from client (because this client has surely been registered to the server at this point)
     * and asks the view to show the lobby waiting room, with the updated info
     * @param playerUsernames the usernames of the players currently connected to the lobby
     * @param gameSettings    the game settings of the lobby
     */
    public void showUpdatedLobby(String[] playerUsernames, GameSettings gameSettings) throws IOException {
        
        if (pingTimer == null) startPing();
        this.gameSettings = gameSettings;
        view.showUpdatedLobby(playerUsernames, gameSettings);
    }
    
    /**
     * Asks the view to show the lobby waiting room, with the updated info
     * @param playerUsernames the usernames of the players currently connected to the lobby
     */
    public void showUpdatedLobby(String[] playerUsernames) throws IOException {
        
        view.showUpdatedLobby(playerUsernames, gameSettings);
    }
    
    /**
     * Asks the view to start the game
     * @param players  the players of the game
     * @param gameMode the mode of the game
     */
    public void startGame(List<Player> players, GameMode gameMode) throws IOException {
        
        view.startGame(players, gameMode);
    }
    
    /**
     * Asks the view to end the game
     * @param team winning team
     */
    public void endGame(Team team) {
        
        view.endGame(team);
    }
    
    /**
     * Asks the view to apply the given update to the game
     * @param update the update to apply
     */
    public void applyUpdate(Update update) {
        
        view.applyUpdate(update);
    }
    
    /**
     * Asks the view to satisfy the given move request
     * @param moveRequestMessage the received request message
     */
    public void requestMove(MoveRequestMessage moveRequestMessage) {
        
        view.requestMove(moveRequestMessage);
    }
    
    public String getUsername() {
        
        return username;
    }
    
    public void setUsername(String username) {
        
        this.username = username;
    }
    
    /**
     * @return the client's log. The log could be showed on the view or saved to file
     */
    public String getLog() {
        
        return log.toString();
    }
    
    /**
     * @return the last lines of the client's log. The log could be showed on the view or saved to file
     */
    public String getLog(int lines) {
        
        StringBuilder stringBuilder = new StringBuilder();
        String[] logLines = log.toString().split("\n");
        for (int n = Math.max(logLines.length - lines, 0); n < logLines.length; n++)
            stringBuilder.append(logLines[n]).append("\n");
        return stringBuilder.toString();
    }
    
    public String loadSavedServerIp() {
        
        try {
            
            FileInputStream fileInputStream = new FileInputStream(ADDRESS_FILE_NAME);
            Scanner scanner = new Scanner(fileInputStream);
            String ip = scanner.next();
            scanner.close();
            return ip;
        }
        catch (FileNotFoundException e) {
            
            System.out.println("File not found");
            return "localhost";
        }
    }
    
    public GameSettings getGameSettings() {
        
        return gameSettings;
    }
    
    public boolean isRunning() {
        
        return running;
    }
    
    public void exit(boolean connectionAlive) throws IOException {
        
        if (running) {
            
            running = false;
            
            pingTimer.cancel();
            pingTimer.purge();
            if (connectionAlive) disconnect();
            
            if (view instanceof EriantysCLI) ((EriantysCLI) view).stop();
            else Platform.exit();
        }
    }
    
    /**
     * Sends a message to the server
     * @param message the message to send
     * @throws IOException if an I/O exception occurs
     */
    public void sendToServer(Message message) throws IOException {
        
        synchronized (serverOutputStream) {
            
            try {
                
                serverOutputStream.writeObject(message);
                log("Message sent: " + message.getClass().getSimpleName());
            }
            catch (SocketException e) {
                
                System.out.println("Server socket closed: couldn't send " + message.getClass().getSimpleName());
                if (running) exit(false);
            }
            
            serverOutputStream.notifyAll();
        }
    }
    
    /**
     * Starts listening to the server in a separate thread
     */
    private void listenToServer() {
        
        new Thread(() -> {
            
            try {
                
                //update loop
                while (running) {
                    
                    Optional<Message> message = readMessage();
                    if (message.isPresent()) {
                        
                        log("Message received: " + message.get().getClass().getSimpleName());
                        message.get().manageAndReply();
                    }
                }
            }
            catch (SocketException e) {
                
                System.out.println("Could not listen to a closed socket: " + server);
                
                try {
                    
                    if (running) exit(false);
                }
                catch (IOException ex) {
                    
                    ex.printStackTrace();
                }
            }
            catch (ClassNotFoundException | IOException e) {
                
                e.printStackTrace();
            }
        }).start();
    }
    
    /**
     * @return An optional containing the message received from the server if there's any. An empty optional otherwise.
     * This method can block
     * @throws IOException            if an I/O exception occurs
     * @throws ClassNotFoundException if the received object was not a message
     */
    private Optional<Message> readMessage() throws IOException, ClassNotFoundException {
        
        try {
            
            Message message = (Message) serverInputStream.readObject();
            return Optional.of(message);
        }
        catch (EOFException e) {
            
            return Optional.empty();
        }
    }
    
    /**
     * Starts a series of ping messages (one every 15 seconds) to check for server disconnections.
     */
    private void startPing() {
        
        if (pingTimer != null) throw new RuntimeException("Couldn't start ping because pings were already started");
        pingTimer = new Timer();
        pingTimer.schedule(new TimerTask() {
            
            @Override
            public void run() {
                
                try {
                    
                    PingMessage sent = new PingMessage(username);
                    sendToServer(sent);
                    sent.waitForValidResponse();
                }
                catch (IOException | InterruptedException e) {
                    
                    e.printStackTrace();
                }
            }
        }, 0, 15000);
    }
    
    /**
     * Disconnects from the server
     * @throws IOException if an I/O exception occurs
     */
    private void disconnect() throws IOException {
        
        if (server != null) {
            
            sendToServer(new DisconnectMessage());
            server.close();
            log("Disconnected from the server");
        }
    }
}
