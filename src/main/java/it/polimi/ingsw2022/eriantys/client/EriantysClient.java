package it.polimi.ingsw2022.eriantys.client;

import it.polimi.ingsw2022.eriantys.client.view.View;
import it.polimi.ingsw2022.eriantys.client.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.client.view.gui.EriantysGUI;
import it.polimi.ingsw2022.eriantys.messages.Message;
import it.polimi.ingsw2022.eriantys.messages.Move.MoveType;
import it.polimi.ingsw2022.eriantys.messages.toClient.MoveRequestMessage;
import it.polimi.ingsw2022.eriantys.messages.toClient.ToClientMessage;
import it.polimi.ingsw2022.eriantys.messages.toClient.changes.Update;
import it.polimi.ingsw2022.eriantys.messages.toServer.GameSettings;
import it.polimi.ingsw2022.eriantys.server.EriantysServer;
import it.polimi.ingsw2022.eriantys.server.controller.Mode;
import it.polimi.ingsw2022.eriantys.server.model.players.Player;
import it.polimi.ingsw2022.eriantys.server.model.players.Team;

import java.io.*;
import java.net.Socket;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

/**
 * This class represents the client of the game. The client runs the view in its main thread and communicates with the server through
 * messages in a secondary thread. The client will manage and modify the view based on the communication with the server.
 * @see View
 * @see EriantysServer
 * @see Message
 * @author Niccolò Nicolosi
 * @author Francesco Melegati Maccari
 * @author Emanuele Musto
 */
public class EriantysClient {

    /**
     * Launches the client with the given arguments
     * @param args the arguments passed to the client. If args contains "-nogui" the cli version of the view will be launched
     * @throws IOException if an I/O exception occurs
     * @throws TimeoutException if the view doesn't respond
     */
    public static void launch(String[] args) throws IOException, TimeoutException {

        boolean gui = args.length == 0 || !args[0].equals("-nogui");
        boolean showLog = (gui && args.length >= 1 && args[0].equals("-log")) || (!gui && args.length >= 2 && args[1].equals("-log"));
        createInstance(gui, showLog);
        getInstance().start();
    }

    private static EriantysClient instance;

    /**
     * Initializes the singleton instance
     * @param gui whether the gui version of the view or the cli one should be launched
     * @param showLog whether view should show logs
     * @throws IOException if could not correctly launch the view
     */
    private static void createInstance(boolean gui, boolean showLog) throws IOException {

        instance = new EriantysClient(gui, showLog);
    }

    /**
     * @return the singleton instance
     */
    public static EriantysClient getInstance() {

        return instance;
    }

    private Socket server;
    private ObjectOutputStream serverOutputStream;
    private ObjectInputStream serverInputStream;
    private String username;

    private final boolean showLog;
    private final StringWriter log;
    private final PrintWriter logWriter;

    private final View view;

    private GameSettings gameSettings;

    private EriantysClient(boolean gui, boolean showLog) throws IOException {

        this.showLog = showLog;
        log = new StringWriter();
        logWriter    = new PrintWriter(log);
        if(!gui) Thread.setDefaultUncaughtExceptionHandler((t, e) -> e.printStackTrace(logWriter));

        //initialize view
        view = gui ? new EriantysGUI() : new EriantysCLI();
    }

    private void start() throws TimeoutException {

        view.start(showLog);
    }

    /**
     * Starts the connection to the server and remain listening in a separate thread
     */
    public void connectToServer() {

        new Thread(() -> {

            try {

                String host = "localhost";
                int port = EriantysServer.PORT_NUMBER;
                log("connecting to " + host + ":" + port);
                server = new Socket(host, port);

                //initialize object streams
                serverOutputStream = new ObjectOutputStream(server.getOutputStream());
                serverInputStream = new ObjectInputStream(server.getInputStream());

                //update loop
                while(true) {

                    Optional<ToClientMessage> message = readMessage();
                    if(message.isPresent()) {

                        log("message received: " + message.get().getClass().getSimpleName());
                        message.get().manageAndReply();
                    }
                }
            }
            catch(ClassNotFoundException | IOException e) {

                log(e.toString());
            }
        }).start();
    }

    /**
     * @return An optional containing the message received from the server if there's any. An empty optional otherwise.
     * This method can block
     * @throws IOException if an I/O exception occurs
     * @throws ClassNotFoundException if the received object was not a message
     */
    private Optional<ToClientMessage> readMessage() throws IOException, ClassNotFoundException {

        try {

            ToClientMessage message = (ToClientMessage) serverInputStream.readObject();
            return Optional.of(message);
        }
        catch(EOFException e) {

            return Optional.empty();
        }
    }

    /**
     * Sends a message to the server
     * @param message the message to send
     * @throws IOException if an I/O exception occurs
     */
    public void sendToServer(Message message) throws IOException {

        serverOutputStream.writeObject(message);
    }

    /**
     * Disconnects from the server
     * @throws IOException if an I/O exception occurs
     */
    public void disconnect() throws IOException {

        server.close();
    }

    /**
     * Asks the view to provide a username and send it to the server
     * @param requestMessage the message requesting the username
     */
    public void askUsername(Message requestMessage) {

        view.askUsername(requestMessage);
    }

    /**
     * Asks the view to provide game settings and send them to the server
     * @param requestMessage the message requesting game settings
     */
    public void askGameSettings(Message requestMessage) {

        view.askGameSettings(requestMessage);
    }

    /**
     * Asks the view to show the lobby waiting room, with the updated info
     * @param playerUsernames the usernames of the players currently connected to the lobby
     * @param gameSettings the game settings of the lobby
     */
    public void showUpdatedLobby(String[] playerUsernames, GameSettings gameSettings) {

        this.gameSettings = gameSettings;
        view.showUpdatedLobby(playerUsernames, gameSettings);
    }

    /**
     * Asks the view to show the lobby waiting room, with the updated info
     * @param playerUsernames the usernames of the players currently connected to the lobby
     */
    public void showUpdatedLobby(String[] playerUsernames) {

        view.showUpdatedLobby(playerUsernames, gameSettings);
    }

    /**
     * Asks the view to start the game
     * @param players the players of the game
     * @param gameMode the mode of the game
     */
    public void startGame(Player[] players, Mode gameMode) {

        view.startGame(players, gameMode);
    }

    /**
     * Asks the view to apply the given update to the game
     * @param update the update to apply
     */
    public void applyUpdate(Update update) {

        update.applyChanges(view);
    }

    /**
     * Asks the view to make a given move
     * @param requestMessage the message containing the requested move
     */
    public void askMoveType(MoveRequestMessage requestMessage){
        view.askMoveType(requestMessage);
    }

    /**
     * Appends the given logText to the log
     * @param logText the text to append
     */
    public void log(String logText) {

        logWriter.print(logText);
        logWriter.print('\n');
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
        for(int n = Math.max(logLines.length - lines, 0); n < logLines.length; n++) stringBuilder.append(logLines[n]).append("\n");
        return stringBuilder.toString();
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername(){
        return username;
    }
}