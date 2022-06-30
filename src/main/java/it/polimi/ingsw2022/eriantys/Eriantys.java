package it.polimi.ingsw2022.eriantys;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.server.EriantysServer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Based on the arguments received starts the server, the client with GUI, or the client with CLI.
 *
 * @author Niccolò Nicolosi
 */
public class Eriantys {
    
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        
        if (args.length > 0 && args[0].equals("-server")) EriantysServer.launch(args);
        else EriantysClient.launch(args);
    }
}
