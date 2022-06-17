package it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.components;

import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.components.AnimatedCLIComponent;
import it.polimi.ingsw2022.eriantys.server.EriantysServer;
import it.polimi.ingsw2022.eriantys.server.model.players.Player;
import it.polimi.ingsw2022.eriantys.server.model.players.Team;

import java.util.List;

import static it.polimi.ingsw2022.eriantys.client.view.cli.AnsiCodes.*;

public class WinnerMessageCLIComponent extends AnimatedCLIComponent {

    private static final String color = RESET;
    private final String[] colors;

    private Team winnerTeam;

    public WinnerMessageCLIComponent() {

        super(EriantysServer.MAX_USERNAME_LENGTH + 6, 8);
        colors = new String[5];
        colors[0] = RED;
        colors[1] = YELLOW;
        colors[2] = GREEN;
        colors[3] = BLUE;
        colors[4] = PURPLE;

        winnerTeam = null;
        buildRows();
    }

    private void buildRows() {

        StringBuilder coloredRow = new StringBuilder(" ");
        for(int n = 0; n < EriantysServer.MAX_USERNAME_LENGTH + 4; n++) coloredRow.append(colors[n % colors.length]).append("_");
        coloredRow.append(RESET).append(color).append(" ");
        setRow(0, color + coloredRow + RESET);

        coloredRow = new StringBuilder();
        coloredRow.append(colors[0]).append("|");
        for(int n = 0; n < EriantysServer.MAX_USERNAME_LENGTH + 4; n++) coloredRow.append(colors[n % colors.length]).append("*");
        coloredRow.append(colors[(EriantysServer.MAX_USERNAME_LENGTH + 4) % colors.length]).append("|");
        setRow(1, color + coloredRow + RESET);

        setRow(2, colors[4] + "|*" + color +
                  "  THE WINNER IS TEAM  "
                  + colors[(getWidth() - 2) % colors.length] + "*|" + RESET);

        if(winnerTeam != null) {

            String team = winnerTeam.getTeamName();
            if(team.length() < 5) team = team + " ".repeat(5 - team.length());
            setRow(3, colors[3] + "|*" + color +
                    "         " + winnerTeam.ansiColor + team + color + "        "
                      + colors[(getWidth() - 1) % colors.length] + "*|" + RESET);
        }
        else setRow(3, colors[3] + "|*" + color +
                       " ".repeat(EriantysServer.MAX_USERNAME_LENGTH + 2)
                       + colors[(getWidth() - 1) % colors.length] + "*|" + RESET);

        setRow(4, colors[2] + "|*" + color +
                  " ".repeat(EriantysServer.MAX_USERNAME_LENGTH + 2)
                  + colors[getWidth() % colors.length] + "*|" + RESET);

        if(winnerTeam != null) {

            List<Player> winners = winnerTeam.getPlayers();

            setRow(5, buildWinnerRow(winners.get(0).username, 1, (getWidth() + 1) % colors.length));
            setRow(6, winners.size() > 1 ?
                      buildWinnerRow(winners.get(1).username, 0, (getWidth() + 2) % colors.length) :
                      colors[0] + "|*" + color +
                      " ".repeat(EriantysServer.MAX_USERNAME_LENGTH + 2)
                      + colors[(getWidth() + 2) % colors.length] + "*|" + RESET);
        }
        else {

            setRow(5, colors[1] + "|*" + color +
                      " ".repeat(EriantysServer.MAX_USERNAME_LENGTH + 2)
                      + colors[(getWidth() + 1) % colors.length] + "*|" + RESET);
            setRow(6, colors[0] + "|*" + color +
                      " ".repeat(EriantysServer.MAX_USERNAME_LENGTH + 2)
                      + colors[(getWidth() + 2) % colors.length] + "*|" + RESET);
        }

        int startColorNumber = 4;
        String[] orderedColors = new String[EriantysServer.MAX_USERNAME_LENGTH + 4];
        for(int n = 0; n < EriantysServer.MAX_USERNAME_LENGTH + 4; n++)
            orderedColors[EriantysServer.MAX_USERNAME_LENGTH + 4 - 1 - n] = colors[(n + startColorNumber) % colors.length];
        coloredRow = new StringBuilder().append(orderedColors[0]).append("|").append(UNDERLINED);
        for(String coloredStar : orderedColors) coloredRow.append(coloredStar).append("*");
        coloredRow.append(RESET).append(orderedColors[orderedColors.length - 1]).append("|");
        setRow(7, color + coloredRow + RESET);
    }

    @Override
    protected void update() {

        super.update();
        String[] temp = new String[colors.length];
        System.arraycopy(colors, 0, temp, 0, colors.length);
        for(int n = 0; n < colors.length; n++) colors[n] = temp[(n + 1) % temp.length];
        buildRows();
    }

    private String buildWinnerRow(String winner, int colorNumberLeft, int colorNumberRight) {

        if(winner.length() % 2 != 0) winner = winner + " ";
        int padding = (getWidth() - 4 - winner.length()) / 2;
        return colors[colorNumberLeft] + "|*" + color +
               " ".repeat(padding) + winner + " ".repeat(padding)
               + colors[colorNumberRight] + "*|" + RESET;
    }

    public void setWinnerTeam(Team winnerTeam) {

        this.winnerTeam = winnerTeam;
    }
}
