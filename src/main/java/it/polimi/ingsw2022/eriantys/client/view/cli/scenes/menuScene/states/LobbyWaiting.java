package it.polimi.ingsw2022.eriantys.client.view.cli.scenes.menuScene.states;

import it.polimi.ingsw2022.eriantys.client.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.client.view.cli.Input;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.components.BasicCLIComponent;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.menuScene.MenuScene;
import it.polimi.ingsw2022.eriantys.messages.toServer.GameSettings;

import java.io.IOException;

public class LobbyWaiting extends MenuSceneState {

    private final String[] playerUsernames;
    private final GameSettings gameSettings;

    public LobbyWaiting(EriantysCLI cli, MenuScene scene, String[] playerUsernames, GameSettings gameSettings) {

        super(cli, scene);
        this.playerUsernames = playerUsernames;
        this.gameSettings    = gameSettings;
    }

    @Override
    public void enter() {

        getScene().getPanel().setHidden(false);

        String gameMode = gameSettings.gameMode.toString();
        getScene().setChosenGameMode(new BasicCLIComponent(22 + gameMode.length(),
                new String[] { "Game mode: " + gameMode + " Players: " + gameSettings.numberOfPlayers}));

        BasicCLIComponent[] players = new BasicCLIComponent[2 + playerUsernames.length];
        players[0] = new BasicCLIComponent(17, new String[] { "Players in lobby:"});
        for(int n = 0; n < playerUsernames.length; n++)
            players[n + 1] = new BasicCLIComponent(playerUsernames[n].length(), new String[] { playerUsernames[n] });
        players[players.length - 1] = new BasicCLIComponent(29,
                new String[] { "Waiting for " + (gameSettings.numberOfPlayers - playerUsernames.length) + " more players..."});
        getScene().setPlayerUsernames(players);
    }

    @Override
    public void exit() {

        getScene().getPanel().setHidden(true);
        getScene().setChosenGameMode(null);
        getScene().setPlayerUsernames(null);
    }

    @Override
    public void manageInput(Input input) throws IOException {}
}
