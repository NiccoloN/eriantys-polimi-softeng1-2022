package it.polimi.ingsw2022.eriantys.view.cli.components;

import java.security.InvalidParameterException;

public class HelperCardCLIComponent extends CLIComponent {

    private final int index;
    private final int priority, movement;

    public HelperCardCLIComponent(int index, int priority, int movement) {

        super(7, 5);

        if (index < 0 || index > 10) throw new InvalidParameterException("Index must be between 1 and 10");
        this.index = index;

        if (priority < 0 || priority > 99) throw new InvalidParameterException("Priority must be between 1 and 99");
        this.priority = priority;

        if (movement < 0 || movement > 99) throw new InvalidParameterException("Movement must be between 1 and 99");
        this.movement = movement;

        buildRows();
    }

    private void buildRows() {

        setRow(0, " _____ ");
        setRow(1, "|" + String.format("%02d", priority) + " " + String.format("%02d", movement) + "|");
        setRow(2, "|  o  |");
        setRow(3, "| (_) |");
        setRow(4, "|_____|");
    }

    public int getIndex() {

        return index;
    }
}