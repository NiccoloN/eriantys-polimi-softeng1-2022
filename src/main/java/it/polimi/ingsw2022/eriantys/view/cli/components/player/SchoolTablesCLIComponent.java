package it.polimi.ingsw2022.eriantys.view.cli.components.player;

import it.polimi.ingsw2022.eriantys.view.cli.components.CLIComponent;

import java.security.InvalidParameterException;

import static it.polimi.ingsw2022.eriantys.view.cli.components.AnsiColorCodes.*;

public class SchoolTablesCLIComponent extends CLIComponent {

    private static final int NICKNAME_MAX_CHARS = 15;
    private static final int TABLE_LENGTH = 21;

    private String color;
    private String nickname;
    private boolean redProf, greenProf, yellowProf, blueProf, pinkProf;
    private int red, green, yellow, blue, pink;

    public SchoolTablesCLIComponent(String nickname) {

        super(33, 9);

        color = PlayerStatusCLIComponent.PLAYER_STATUS_DEFAULT_COLOR;

        if (nickname.length() == 0) throw new InvalidParameterException("Nickname must be at least 1 character");
        if (nickname.length() > NICKNAME_MAX_CHARS) throw new InvalidParameterException("Nickname must be at most" + NICKNAME_MAX_CHARS + " characters");
        this.nickname = nickname;

        redProf = false;
        greenProf = true;
        yellowProf = false;
        blueProf = false;
        pinkProf = false;
        red = 4;
        green = 10;
        yellow = 1;
        blue = 5;
        pink = 7;

        buildRows();
    }

    private void buildRows() {

        setRow(0, color + " _" + nickname + "_".repeat(getWidth() - nickname.length() - 3) + " ");
        setRow(1, "|  " + "_".repeat(TABLE_LENGTH) + "   ___  |");
        setRow(2, buildTableRow(GREEN_BACKGROUND_BRIGHT, green, greenProf));
        setRow(3, buildTableRow(RED_BACKGROUND, red, redProf));
        setRow(4, buildTableRow(YELLOW_BACKGROUND, yellow, yellowProf));
        setRow(5, buildTableRow(PURPLE_BACKGROUND_BRIGHT, pink, pinkProf));
        setRow(6, buildTableRow(BLUE_BACKGROUND_BRIGHT, blue, blueProf));
        setRow(7, "|  " + (" ".repeat(5) + YELLOW + "C" + color).repeat(3) + "           |");
        setRow(8, "|" + "_".repeat(getWidth() - 2) + "|");
    }
    
    private String buildTableRow(String ansiColor, int students, boolean prof) {
        
        StringBuilder row = new StringBuilder("| |_");
        for (int n = 0; n < students; n++) row.append(ansiColor).append("_").append(color).append("_");
        row.append("__".repeat(Math.max(0, 10 - students)));
        row.append("| |_").append(prof ? ansiColor : color).append("_").append(color).append("_| |");
        return row.toString();
    }
}
