package it.polimi.ingsw2022.eriantys.client.view.cli.scenes.endGameScene;

import it.polimi.ingsw2022.eriantys.client.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.client.view.cli.Frame;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.CLIScene;
import it.polimi.ingsw2022.eriantys.server.model.players.Team;

public class EndGameScene extends CLIScene {

    private Team winningTeam;

    public EndGameScene(EriantysCLI cli, int width, int height, Team winningTeam) {
        super(cli, width, height);
        this.winningTeam = winningTeam;
    }

    @Override
    public void printToFrame(Frame frame) {

    }
}
