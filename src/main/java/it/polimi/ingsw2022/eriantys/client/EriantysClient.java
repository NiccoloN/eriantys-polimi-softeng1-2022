package it.polimi.ingsw2022.eriantys.client;

import it.polimi.ingsw2022.eriantys.client.view.View;
import it.polimi.ingsw2022.eriantys.client.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.client.view.gui.EriantysGUI;
import it.polimi.ingsw2022.eriantys.messages.Message;
import it.polimi.ingsw2022.eriantys.messages.toClient.ToClientMessage;
import it.polimi.ingsw2022.eriantys.messages.toClient.changes.Update;
import it.polimi.ingsw2022.eriantys.messages.toServer.GameSettings;
import it.polimi.ingsw2022.eriantys.server.EriantysServer;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

/**
 * @author Niccolò Nicolosi
 * @author Francesco Melegati Maccari
 * @author Emanuele Musto
 */
public class EriantysClient {

    public static void launch(String[] args) throws IOException, TimeoutException {

        boolean gui = args.length <= 0 || !args[0].equals("-nogui");
        createInstance(gui);
        getInstance().start();
    }

    private static EriantysClient instance;

    private static void createInstance(boolean gui) throws IOException {

        instance = new EriantysClient(gui);
    }

    public static EriantysClient getInstance() {

        return instance;
    }

    private Socket server;
    private ObjectOutputStream serverOutputStream;
    private ObjectInputStream serverInputStream;

    private StringBuilder log;

    private final View view;

    private EriantysClient(boolean gui) throws IOException {

        log = new StringBuilder();

        //initialize view
        view = gui ? new EriantysGUI() : new EriantysCLI();
    }

    private void start() throws TimeoutException {

        view.start();
    }

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

                e.printStackTrace();
            }
        }).start();
    }

    private Optional<ToClientMessage> readMessage() throws IOException, ClassNotFoundException {

        try {

            ToClientMessage message = (ToClientMessage) serverInputStream.readObject();
            return Optional.of(message);
        }
        catch(EOFException e) {

            return Optional.empty();
        }
    }

    public void sendToServer(Message message) throws IOException {

        serverOutputStream.writeObject(message);
    }

    public void disconnect() throws IOException {

        server.close();
    }

    public void askUsername(Message requestMessage) {

        view.askUsername(requestMessage);
    }

    public void askGameSettings(Message requestMessage) {

        view.askGameSettings(requestMessage);
    }

    public void startGame() {

        view.startGame();
    }

    public void applyUpdate(Update update) {

        update.applyChanges(view);
    }

    public void log(String logText) {

        log.append(logText).append('\n');
    }

    public void clearLog() {

        log = new StringBuilder();
    }

    public String getLog() {

        return log.toString();
    }
}