package it.polimi.ingsw2022.eriantys.view.cli.components;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.Observable;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static it.polimi.ingsw2022.eriantys.view.cli.components.AnsiColorCodes.*;

/**
 * This class represents a cli component. A cli component is an array of strings of the same length to be printed at
 * its position on the terminal window
 */
public class CLIComponent {

    private int x, y;
    private final int width, height;
    private final String[][] chars;

    /**
     * Constructs a cli component from the given rows
     * @param width the width of the component
     * @param rows the rows from which to construct the component
     * @throws InvalidParameterException if any of the given rows is not of the given width
     */
    public CLIComponent(int width, String[] rows) {

        this(width, rows.length);
        for (int n = 0; n < rows.length; n++) setRow(n, rows[n]);
    }

    /**
     * Constructs an empty cli component of the given size
     * @param width the width of the component
     * @param height the height of the component
     */
    public CLIComponent(int width, int height) {

        if (width < 1) throw new InvalidParameterException("Width must be >= 1");
        this.width = width;
        if (height < 1) throw new InvalidParameterException("Height must be >= 1");
        this.height = height;
        x = 0;
        y = 0;

        chars = new String[height][width];
        for (int n = 0; n < chars.length; n++) Arrays.fill(chars[n], " ");
    }

    public int getX() {

        return x;
    }

    public void setX(int x) {

        this.x = x;
    }

    public int getY() {

        return y;
    }

    public void setY(int y) {

        this.y = y;
    }

    public void setPosition(int x, int y) {

        setX(x);
        setY(y);
    }

    public int getWidth() {

        return width;
    }

    public int getHeight() {

        return height;
    }

    public void printToFrame(String[][] frame) {

        for (int i = 0; i < height; i++) System.arraycopy(chars[i], 0, frame[y + i], x, width);
    }

    /**
     * Sets the row of the given index
     * @param index the index of the row to set
     * @param row the new row
     * @throws InvalidParameterException if the given row is not of the right length
     * (ansi escape sequences are not considered in part of its length)
     */
    protected void setRow(int index, String row) {

        int length = 0;

        String currentChar = "";
        boolean toConsider = true;
        char[] chars = row.toCharArray();
        for (int n = 0; n < chars.length; n++) {

            currentChar += chars[n];

            if (chars[n] == 27) toConsider = false;
            if (toConsider) {

                this.chars[index][length] = currentChar;
                currentChar = "";
                length++;
            }
            if (chars[n] == 'm') toConsider = true;
        }
        this.chars[index][length - 1] += currentChar;

        if (length != width)
            throw new InvalidParameterException("Row must be of length " + width +
                    ", like declared (actual length: " + length + ")");
    }

    public void setColor(String ansiColor) {

        for (int n = 0; n < chars.length; n++) {

            if (chars[n][0].startsWith(String.valueOf(ESCAPE_CHAR))) chars[n][0] = ansiColor + chars[n][0].substring(chars[n][0].indexOf('m') + 1);
            else chars[n][0] = ansiColor + chars[n][0];

            if (!chars[n][width - 1].endsWith(RESET)) chars[n][width - 1] = chars[n][width - 1] + RESET;
        }
    }
}
