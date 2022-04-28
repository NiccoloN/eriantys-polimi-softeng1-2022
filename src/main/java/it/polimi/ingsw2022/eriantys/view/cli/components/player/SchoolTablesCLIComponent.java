package it.polimi.ingsw2022.eriantys.view.cli.components.player;

import it.polimi.ingsw2022.eriantys.view.cli.Frame;
import it.polimi.ingsw2022.eriantys.view.cli.components.CLIComponent;

import java.security.InvalidParameterException;

import static it.polimi.ingsw2022.eriantys.view.cli.AnsiCodes.*;

class SchoolTablesCLIComponent extends CLIComponent {

    private static final int NICKNAME_MAX_CHARS = 15;
    private static final int TABLE_LENGTH = 21;

    private String color;
    private String nickname;
    private boolean redProf, greenProf, yellowProf, blueProf, pinkProf;
    private int red, green, yellow, blue, pink;

    SchoolTablesCLIComponent(String nickname) {

        super(33, 8);

        color = PlayerStatusCLIComponent.PLAYER_STATUS_DEFAULT_COLOR;

        setNickname(nickname);

        redProf = false;
        greenProf = false;
        yellowProf = false;
        blueProf = false;
        pinkProf = false;
        red = 0;
        green = 0;
        yellow = 0;
        blue = 0;
        pink = 0;

        buildRows();
    }

    private void buildRows() {

        setRow(0, color + " _" + nickname + "_".repeat(getWidth() - nickname.length() - 3) + " " + RESET);
        setRow(1, color + "|  " + ("_____" + YELLOW + "_" + RESET + color).repeat(3) + "___   ___  |" + RESET);
        setRow(2, buildTableRow(GREEN_BACKGROUND_BRIGHT, green, greenProf));
        setRow(3, buildTableRow(RED_BACKGROUND, red, redProf));
        setRow(4, buildTableRow(YELLOW_BACKGROUND, yellow, yellowProf));
        setRow(5, buildTableRow(PURPLE_BACKGROUND_BRIGHT, pink, pinkProf));
        setRow(6, buildTableRow(BLUE_BACKGROUND_BRIGHT, blue, blueProf));
        setRow(7, color + "|" + "_".repeat(getWidth() - 2) + "|" + RESET);
    }
    
    private String buildTableRow(String ansiColor, int students, boolean prof) {
        
        StringBuilder row = new StringBuilder(color);
        row.append("| |");
        for (int n = 0; n < 10; n++) {

            row.append("_");
            if (n < students) row.append(ansiColor);
            if ((n + 1) % 3 == 0) {

                row.append(YELLOW).append(UNDERLINED);
                if (n >= students) row.append("C");
                else row.append("_");
            }
            else row.append("_");

            row.append(RESET).append(color);
        }
        row.append("_| |_").append(prof ? ansiColor : RESET + color).append("_").append(RESET).append(color).append("_| |").append(RESET);
        return row.toString();
    }

    @Override
    public void printToFrame(Frame frame) {

        buildRows();
        super.printToFrame(frame);
    }

    @Override
    public void setColor(String color) {

        this.color = color;
    }

    void setNickname(String nickname) {

        if (nickname.length() == 0) throw new InvalidParameterException("Nickname must be at least 1 character");
        if (nickname.length() > NICKNAME_MAX_CHARS) throw new InvalidParameterException("Nickname must be at most" + NICKNAME_MAX_CHARS + " characters");
        this.nickname = nickname;
    }

    void setRed(int red) {

        if (red < 0 || red > 10) throw new InvalidParameterException("Red must be >= 0 and <= 10");
        this.red = red;
    }

    void setGreen(int green) {

        if (green < 0 || green > 10) throw new InvalidParameterException("Green must be >= 0 and <= 10");
        this.green = green;
    }

    void setYellow(int yellow) {

        if (yellow < 0 || yellow > 10) throw new InvalidParameterException("Yellow must be >= 0 and <= 10");
        this.yellow = yellow;
    }

    void setBlue(int blue) {

        if (blue < 0 || blue > 10) throw new InvalidParameterException("Blue must be >= 0 and <= 10");
        this.blue = blue;
    }

    void setPink(int pink) {

        if (pink < 0 || pink > 10) throw new InvalidParameterException("Pink must be >= 0 and <= 10");
        this.pink = pink;
    }

    void setRedProf(boolean redProf) {
        
        this.redProf = redProf;
    }

    void setGreenProf(boolean greenProf) {

        this.greenProf = greenProf;
    }
    
    void setYellowProf(boolean yellowProf) {

        this.yellowProf = yellowProf;
    }
    
    void setBlueProf(boolean blueProf) {

        this.blueProf = blueProf;
    }

    void setPinkProf(boolean pinkProf) {

        this.pinkProf = pinkProf;
    }
}
