package it.polimi.ingsw2022.eriantys.view.cli.components;

import java.security.InvalidParameterException;
import java.util.Arrays;
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
    private final String[] rows;

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

        x = 0;
        y = 0;
        this.width = width;
        this.height = height;

        String[] rows = new String[height];
        Arrays.fill(rows, " ".repeat(width));
        this.rows = rows;
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

    public String[] getRows() {

        return Arrays.copyOf(rows, rows.length);
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

        boolean toConsider = true;
        char[] chars = row.toCharArray();
        for (int n = 0; n < chars.length; n++) {

            if (chars[n] == 27) toConsider = false;
            if (toConsider) length++;
            if (chars[n] == 'm') toConsider = true;
        }

        if (length != width)
            throw new InvalidParameterException("Row must be of length " + width +
                    ", like declared (actual length: " + length + ")");
        rows[index] = row;
    }

    public void setColor(String ansiColor) {

        if (rows[0].startsWith(String.valueOf(ESCAPE_CHAR))) rows[0] = ansiColor + rows[0].substring(rows[0].indexOf('m') + 1);
        else rows[0] = ansiColor + rows[0];

        if (!rows[rows.length - 1].endsWith(RESET)) rows[rows.length - 1] = rows[rows.length - 1] + RESET;
    }
}
