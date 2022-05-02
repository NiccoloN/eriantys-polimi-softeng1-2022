package it.polimi.ingsw2022.eriantys.client.view.cli.components;

import it.polimi.ingsw2022.eriantys.client.view.cli.Frame;

import java.security.InvalidParameterException;

import static it.polimi.ingsw2022.eriantys.client.view.cli.AnsiCodes.*;

/**
 * This class represents a character card cli component
 * @author Niccolò Nicolosi
 */
public class CharacterCardCLIComponent extends BasicCLIComponent {

    public static final String CHARACTER_DEFAULT_COLOR = RESET;

    private String color;
    private final int index;
    private final String effect;
    private int cost;

    /**
     * Constructs a character card cli component with the given index, effect and cost
     * @param index the index of this character card
     * @param effect the text that describes the effect of this character card
     * @param cost the cost of this character card
     */
    public CharacterCardCLIComponent(int index, String effect, int cost) {

        super(7, 5);

        color = CHARACTER_DEFAULT_COLOR;
        this.index = index;
        this.effect = effect;
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
    public void printToFrame(Frame frame) {

        buildRows();
        super.printToFrame(frame);
    }

    @Override
    public void setColor(String ansiColor) {

        this.color = ansiColor;
    }

    /**
     * @return the index of this character card
     */
    public int getIndex() {

        return index;
    }

    /**
     * @return the text that describes the effect of this character card
     */
    public String getEffect() {

        return effect;
    }

    /**
     * @return the cost of this character card
     */
    public int getCost() {

        return cost;
    }

    /**
     * Sets the cost to visualize on this card
     * @param cost the new cost
     */
    public void setCost(int cost) {

        if (cost < 0 || cost > 99) throw new InvalidParameterException("Cost must be between 1 and 99");
        this.cost = cost;
    }
}
