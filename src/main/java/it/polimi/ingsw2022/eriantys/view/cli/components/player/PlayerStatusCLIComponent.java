package it.polimi.ingsw2022.eriantys.view.cli.components.player;

import it.polimi.ingsw2022.eriantys.view.cli.components.CLIComponent;

import java.security.InvalidParameterException;

import static it.polimi.ingsw2022.eriantys.view.cli.components.AnsiColorCodes.RESET;

public class PlayerStatusCLIComponent extends CLIComponent {

    static final String PLAYER_STATUS_DEFAULT_COLOR = RESET;

    private final int index;
    private final SchoolTablesCLIComponent tablesCLIComponent;
    private final PlayerStatsCLIComponent statsCLIComponent;

    public PlayerStatusCLIComponent(int index, String nickname, String teamAnsiColor) {

        super(41, 9);

        if (index < 0 || index > 9) throw new InvalidParameterException("Index must be a positive single digit integer");
        this.index = index;

        tablesCLIComponent = new SchoolTablesCLIComponent(nickname);
        statsCLIComponent = new PlayerStatsCLIComponent(teamAnsiColor);
    }

    @Override
    public void printToFrame(String[][] frame) {

        tablesCLIComponent.printToFrame(frame);
        statsCLIComponent.printToFrame(frame);
    }

    @Override
    public void setColor(String ansiColor) {

        tablesCLIComponent.setColor(ansiColor);
        statsCLIComponent.setColor(ansiColor);
    }

    @Override
    public void setX(int x) {

        tablesCLIComponent.setX(x);
        statsCLIComponent.setX(x + tablesCLIComponent.getWidth());
    }

    @Override
    public void setY(int y) {

        tablesCLIComponent.setY(y);
        statsCLIComponent.setY(y);
    }

    public int getIndex() {

        return index;
    }

    public void setNickname(String nickname) {

        tablesCLIComponent.setNickname(nickname);
    }

    public void setRedEntrance(int red) {

        statsCLIComponent.setRed(red);
    }

    public void setGreenEntrance(int green) {

        statsCLIComponent.setGreen(green);
    }

    public void setYellowEntrance(int yellow) {

        statsCLIComponent.setYellow(yellow);
    }

    public void setBlueEntrance(int blue) {

        statsCLIComponent.setBlue(blue);
    }

    public void setPinkEntrance(int pink) {

        statsCLIComponent.setPink(pink);
    }

    public void setRed(int red) {

        tablesCLIComponent.setRed(red);
    }

    public void setGreen(int green) {

        tablesCLIComponent.setGreen(green);
    }

    public void setYellow(int yellow) {

        tablesCLIComponent.setYellow(yellow);
    }

    public void setBlue(int blue) {

        tablesCLIComponent.setBlue(blue);
    }

    public void setPink(int pink) {

        tablesCLIComponent.setPink(pink);
    }

    public void setRedProf(boolean redProf) {

        tablesCLIComponent.setRedProf(redProf);
    }

    public void setGreenProf(boolean greenProf) {

        tablesCLIComponent.setGreenProf(greenProf);
    }

    public void setYellowProf(boolean yellowProf) {

        tablesCLIComponent.setYellowProf(yellowProf);
    }

    public void setBlueProf(boolean blueProf) {

        tablesCLIComponent.setBlueProf(blueProf);
    }
    
    public void setPinkProf(boolean pinkProf) {

        tablesCLIComponent.setPinkProf(pinkProf);
    }
}
