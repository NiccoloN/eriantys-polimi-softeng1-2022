package it.polimi.ingsw2022.eriantys.view.cli.components;

import java.security.InvalidParameterException;

import static it.polimi.ingsw2022.eriantys.view.cli.AnsiCodes.*;

public class CharacterCardCLIComponent extends CLIComponent {

    public static final String CHARACTER_DEFAULT_COLOR = RESET;

    private String color;
    private int cost;

    public CharacterCardCLIComponent(int cost) {

        super(7, 5);

        color = CHARACTER_DEFAULT_COLOR;
        this.cost = cost;
    }

    private void buildRows() {

        setRow(0, color + " _____ " + RESET);
        setRow(1, color + "|" + YELLOW + "C" + RESET + color + ": " + (cost < 10 ? "0" : " ") + cost + "|" + RESET);
        setRow(2, color + "|  o  |" + RESET);
        setRow(3, color + "|_(_)_|" + RESET);
        setRow(4, color + "|_____|" + RESET);
    }

    @Override
    public void printToFrame(String[][] frame) {

        buildRows();
        super.printToFrame(frame);
    }

    @Override
    public void setColor(String ansiColor) {

        this.color = ansiColor;
    }

    public void setCost(int cost) {

        if (cost < 0 || cost > 99) throw new InvalidParameterException("Cost must be between 1 and 99");
        this.cost = cost;
    }
}
