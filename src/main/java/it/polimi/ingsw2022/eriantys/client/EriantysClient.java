package it.polimi.ingsw2022.eriantys.client;

import it.polimi.ingsw2022.eriantys.client.view.View;
import it.polimi.ingsw2022.eriantys.client.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.client.view.gui.EriantysGUI;
import it.polimi.ingsw2022.eriantys.messages.Message;
import it.polimi.ingsw2022.eriantys.server.EriantysServer;
import javafx.application.Application;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

public class EriantysClient {

    public static void launch(String[] args) throws IOException, ClassNotFoundException {

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

    private final View view;

    private EriantysClient(boolean gui) throws IOException {

        //initialize view
        view = gui ? new EriantysGUI() : new EriantysCLI();
    }

    private void start() throws IOException, ClassNotFoundException {

        /*
        //start the view in a separate thread
        new Thread(() -> {

            try {

                view.start();
            }
            catch(TimeoutException e) {

                e.printStackTrace();
            }
        }).start();
         */

        //connect to server
        String host = "localhost";
        int port = EriantysServer.PORT_NUMBER;
        System.out.println("connecting to " + host + ":" + port);
        server = new Socket(host, port);

        //initialize object streams
        serverOutputStream = new ObjectOutputStream(server.getOutputStream());
        serverInputStream = new ObjectInputStream(server.getInputStream());

        //update loop
        while(true) {

            Optional<Message> message = readMessage();
            if(message.isPresent()) {

                System.out.println("message received: " + message.get().getClass().getSimpleName());
                message.get().manageAndReply(server);
            }
        }
    }

    private Optional<Message> readMessage() throws IOException, ClassNotFoundException {

        try {

            Message message = (Message) serverInputStream.readObject();
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

    public String getUsername() {

        return view.getUsername();
    }
}