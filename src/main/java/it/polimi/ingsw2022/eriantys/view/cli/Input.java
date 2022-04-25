package it.polimi.ingsw2022.eriantys.view.cli;

import java.util.Arrays;

public class Input {

    private final char[] inputChars;

    public Input() {

        inputChars = new char[3];
    }

    public Input(char c1, char c2, char c3) {

        this();
        inputChars[0] = c1;
        inputChars[1] = c2;
        inputChars[2] = c3;
    }

    public Input(char c1, char c2) {

        this(c1, c2, '\0');
    }

    public Input(char c1) {

        this(c1, '\0', '\0');
    }

    public boolean ofAction(Action action) {

        for(Input input : action.getInputs()) if(this.equals(input)) return true;
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
