package it.polimi.ingsw2022.eriantys;

import it.polimi.ingsw2022.eriantys.view.EriantysCLI;
import it.polimi.ingsw2022.eriantys.view.EriantysGUI;
import javafx.application.Application;

import java.io.IOException;

public class Eriantys {

    public static void main(String[] args) {

        if(args.length > 0 && args[0].equals("-nogui")) {

            try {

                EriantysCLI cli = new EriantysCLI();
                cli.start();
            }
            catch(IOException e) {

                e.printStackTrace();
            }
        }

        else Application.launch(EriantysGUI.class, args);
    }
}