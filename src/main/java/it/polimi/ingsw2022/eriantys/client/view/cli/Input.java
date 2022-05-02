package it.polimi.ingsw2022.eriantys.client.view.cli;

import java.util.Arrays;

/**
 * This class represents a keyboard input (the press of a single key) read by the terminal. Every input consists of either 1 char, if it
 * corresponds to the press of simple character key, or 3 chars, if it corresponds to the press of a special key that prints an ansi sequence
 * on the terminal (like arrow keys). Different inputs are associated to the specific actions they trigger
 * @see Action
 * @author Niccolò Nicolosi
 */
public class Input {

    private final char[] inputChars;

    /**
     * Constructs an empty input
     */
    public Input() {

        inputChars = new char[3];
    }

    /**
     * Constructs an input consisting of 3 chars
     * @param c1 the first char of the input
     * @param c2 the second char of the input
     * @param c3 the third char of the input
     */
    public Input(char c1, char c2, char c3) {

        this();
        inputChars[0] = c1;
        inputChars[1] = c2;
        inputChars[2] = c3;
    }

    /**
     * Constructs an input consisting of a single char
     * @param c1 the single char of the input
     */
    public Input(char c1) {

        this(c1, '\0', '\0');
    }

    /**
     * @param action the action to check
     * @return whether this input triggers the given action
     */
    public boolean triggersAction(Action action) {

        for(Input input : action.getTriggerInputs()) if(this.equals(input)) return true;
        return false;
    }

    @Override
    public boolean equals(Object o) {

        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Input input = (Input) o;
        return Arrays.equals(inputChars, input.inputChars);
    }

    @Override
    public int hashCode() {

        return Arrays.hashCode(inputChars);
    }

    @Override
    public String toString() {

        int[] charCodes = new int[inputChars.length];
        for(int n = 0; n < inputChars.length; n++) charCodes[n] = inputChars[n];
        return "Input: " + Arrays.toString(charCodes);
    }
}
