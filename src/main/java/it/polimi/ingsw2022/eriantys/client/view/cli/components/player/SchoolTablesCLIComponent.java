package it.polimi.ingsw2022.eriantys.client.view.cli.components.player;

import it.polimi.ingsw2022.eriantys.client.view.cli.Frame;
import it.polimi.ingsw2022.eriantys.client.view.cli.components.BasicCLIComponent;

import java.security.InvalidParameterException;

import static it.polimi.ingsw2022.eriantys.client.view.cli.AnsiCodes.*;

class SchoolTablesCLIComponent extends BasicCLIComponent {

    private static final int NICKNAME_MAX_CHARS = 15;
    private static final int TABLE_LENGTH = 21;

    private String color;
    private String nickname;
    private boolean redProf, greenProf, yellowProf, blueProf, pinkProf;
    private int red, green, yellow, blue, pink;

    /**
     * Constructs a school table cli component with the given nickname
     * @param nickname the nickname to associate to this component
     * @throws InvalidParameterException if the given nickname is not valid
     */
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

    /**
     * Sets the red students visualized on this component
     * @param red the number of red students to visualize
     * @throws InvalidParameterException if red is not between 0 and 10
     */
    void setRed(int red) {

        if (red < 0 || red > 10) throw new InvalidParameterException("Red must be >= 0 and <= 10");
        this.red = red;
    }

    /**
     * Sets the green students visualized on this component
     * @param green the number of green students to visualize
     * @throws InvalidParameterException if green is not between 0 and 10
     */
    void setGreen(int green) {

        if (green < 0 || green > 10) throw new InvalidParameterException("Green must be >= 0 and <= 10");
        this.green = green;
    }

    /**
     * Sets the yellow students visualized on this component
     * @param yellow the number of yellow students to visualize
     * @throws InvalidParameterException if yellow is not between 0 and 10
     */
    void setYellow(int yellow) {

        if (yellow < 0 || yellow > 10) throw new InvalidParameterException("Yellow must be >= 0 and <= 10");
        this.yellow = yellow;
    }

    /**
     * Sets the blue students visualized on this component
     * @param blue the number of blue students to visualize
     * @throws InvalidParameterException if blue is not between 0 and 10
     */
    void setBlue(int blue) {

        if (blue < 0 || blue > 10) throw new InvalidParameterException("Blue must be >= 0 and <= 10");
        this.blue = blue;
    }

    /**
     * Sets the pink students visualized on this component
     * @param pink the number of pink students to visualize
     * @throws InvalidParameterException if pink is not between 0 and 10
     */
    void setPink(int pink) {

        if (pink < 0 || pink > 10) throw new InvalidParameterException("Pink must be >= 0 and <= 10");
        this.pink = pink;
    }

    /**
     * Sets if the red professor is visible
     * @param redProf the visibility of the red professor: visible if true, hidden otherwise
     */
    void setRedProf(boolean redProf) {
        
        this.redProf = redProf;
    }

    /**
     * Sets if the green professor is visible
     * @param greenProf the visibility of the green professor: visible if true, hidden otherwise
     */
    void setGreenProf(boolean greenProf) {

        this.greenProf = greenProf;
    }

    /**
     * Sets if the yellow professor is visible
     * @param yellowProf the visibility of the yellow professor: visible if true, hidden otherwise
     */
    void setYellowProf(boolean yellowProf) {

        this.yellowProf = yellowProf;
    }

    /**
     * Sets if the blue professor is visible
     * @param blueProf the visibility of the blue professor: visible if true, hidden otherwise
     */
    void setBlueProf(boolean blueProf) {

        this.blueProf = blueProf;
    }

    /**
     * Sets if the pink professor is visible
     * @param pinkProf the visibility of the pink professor: visible if true, hidden otherwise
     */
    void setPinkProf(boolean pinkProf) {

        this.pinkProf = pinkProf;
    }
}
