package it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.components;

import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.components.AnimatedCLIComponent;
import it.polimi.ingsw2022.eriantys.server.EriantysServer;
import it.polimi.ingsw2022.eriantys.server.model.players.Player;
import it.polimi.ingsw2022.eriantys.server.model.players.Team;

import java.util.List;

import static it.polimi.ingsw2022.eriantys.client.view.cli.AnsiCodes.*;

public class WinnerMessageCLIComponent extends AnimatedCLIComponent {

    private static final String color = RESET;

    private Team winnerTeam;

    public WinnerMessageCLIComponent() {

        super(EriantysServer.MAX_USERNAME_LENGTH + 4, 8);
        winnerTeam = null;
        buildRows();
    }

    private void buildRows() {

        setRow(0, color + " " + "_".repeat(EriantysServer.MAX_USERNAME_LENGTH + 2) + " " + RESET);
        setRow(1, color + "|" + " ".repeat(EriantysServer.MAX_USERNAME_LENGTH + 2) + "|" + RESET);
        setRow(2, color + "|  THE WINNER IS TEAM  |" + RESET);

        if(winnerTeam != null) {

            String team = winnerTeam.getTeamName();
            if(team.length() < 5) team = team + " ".repeat(5 - team.length());
            setRow(3, color + "|         " + winnerTeam.ansiColor + team + RESET + color + "        |" + RESET);
        }
        else setRow(3, color + "|" + " ".repeat(EriantysServer.MAX_USERNAME_LENGTH + 2) + "|" + RESET);

        setRow(4, color + "|" + " ".repeat(EriantysServer.MAX_USERNAME_LENGTH + 2) + "|" + RESET);

        if(winnerTeam != null) {

            List<Player> winners = winnerTeam.getPlayers();

            setRow(5, buildWinnerRow(winners.get(0).username));
            setRow(6, (winners.size() > 1 ? buildWinnerRow(winners.get(1).username) :
                       color + "|" + " ".repeat(EriantysServer.MAX_USERNAME_LENGTH + 2)) + "|" + RESET);
        }
        else {

            setRow(5, color + "|" + " ".repeat(EriantysServer.MAX_USERNAME_LENGTH + 2) + "|" + RESET);
            setRow(6, color + "|" + " ".repeat(EriantysServer.MAX_USERNAME_LENGTH + 2) + "|" + RESET);
        }

        setRow(7, color + "|" + "_".repeat(EriantysServer.MAX_USERNAME_LENGTH + 2) + "|" + RESET);
    }

    @Override
    protected void update() {

        super.update();
        buildRows();
    }

    private String buildWinnerRow(String winner) {

        if(winner.length() % 2 != 0) winner = winner + " ";
        int padding = (getWidth() - 2 - winner.length()) / 2;
        return color + "|" + " ".repeat(padding) + winner + " ".repeat(padding) + "|" + RESET;
    }

    public void setWinnerTeam(Team winnerTeam) {

        this.winnerTeam = winnerTeam;
    }
}
