package it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.components;

import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.components.BasicCLIComponent;

import java.security.InvalidParameterException;

import static it.polimi.ingsw2022.eriantys.client.view.cli.AnsiCodes.*;

/**
 * This class represents a helper card cli component
 * @author Niccolò Nicolosi
 */
public class HelperCardCLIComponent extends BasicCLIComponent {

    public static final int WIDTH = 7, HEIGHT = 5;

    private final int index;
    private final int priority, movement;
    private boolean playable;

    /**
     * Constructs a helper card cli component with the given index, priority value and movement value
     * @param index the index of this helper card
     * @param priority the priority value of this helper card
     * @param movement the movement value of this helper card
     */
    public HelperCardCLIComponent(int index, int priority, int movement) {

        super(WIDTH, HEIGHT);

        if (index < 0 || index > 10) throw new InvalidParameterException("Index must be between 1 and 10");
        this.index = index;

        if (priority < 0 || priority > 99) throw new InvalidParameterException("Priority must be between 1 and 99");
        this.priority = priority;

        if (movement < 0 || movement > 99) throw new InvalidParameterException("Movement must be between 1 and 99");
        this.movement = movement;

        playable = true;

        buildRows();
    }

    private void buildRows() {

        setRow(0, " _____ ");
        setRow(1, "|" + String.format("%02d", priority) + " " + String.format("%02d", movement) + "|");
        setRow(2, "|  o  |");
        setRow(3, "| (_) |");
        setRow(4, "|_____|");
    }

    /**
     * @return the index of this helper card
     */
    public int getIndex() {

        return index;
    }

    public boolean isPlayable() {

        return playable;
    }

    public void setPlayable(boolean playable) {

        this.playable = playable;
        setColor(playable ? RESET : RED);
    }
}
