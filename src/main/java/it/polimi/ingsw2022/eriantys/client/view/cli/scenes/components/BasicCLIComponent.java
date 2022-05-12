package it.polimi.ingsw2022.eriantys.client.view.cli.scenes.components;

import it.polimi.ingsw2022.eriantys.client.view.cli.Frame;

import java.security.InvalidParameterException;
import java.util.Arrays;

import static it.polimi.ingsw2022.eriantys.client.view.cli.AnsiCodes.*;

/**
 * This class represents a basic cli component. A basic cli component is a bi-dimensional graphic element built of ascii characters
 * @see CLIComponent
 * @author Niccolò Nicolosi
 */
public class BasicCLIComponent implements CLIComponent {

    private float x, y;
    private final int width, height;
    private boolean hidden;
    private final String[][] chars;

    /**
     * Constructs a cli component from the given rows
     * @param width the width of the component
     * @param rows the rows from which to construct the component
     * @throws InvalidParameterException if any of the given rows is not of the given width
     */
    public BasicCLIComponent(int width, String[] rows) {

        this(width, rows.length);
        for (int n = 0; n < rows.length; n++) setRow(n, rows[n]);
    }

    /**
     * Constructs an empty cli component of the given size
     * @param width the width of the component
     * @param height the height of the component
     * @throws InvalidParameterException if width or height are < 1
     */
    public BasicCLIComponent(int width, int height) {

        if (width < 1) throw new InvalidParameterException("Width must be >= 1");
        this.width = width;
        if (height < 1) throw new InvalidParameterException("Height must be >= 1");
        this.height = height;

        x = 0;
        y = 0;
        hidden = false;

        chars = new String[height][width];
        for(String[] row : chars) Arrays.fill(row, " ");
    }

    /**
     * Sets the row of the given index
     * @param index the index of the row to set
     * @param row the new row ('\0' chars corresponds to transparent chars)
     * @throws InvalidParameterException if the given row is not of the right length
     * (ansi escape sequences are not considered in part of its length)
     */
    protected void setRow(int index, String row) {

        char[] chars = row.toCharArray();

        int length = 0;
        String currentChar = "";
        boolean toConsider = true;

        for (int n = 0; n < chars.length; n++) {

            currentChar += chars[n];

            if (chars[n] == ESCAPE_CHAR) toConsider = false;
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

    @Override
    public void setColor(String ansiColor) {

        if(!isAnsiSequence(ansiColor)) throw new InvalidParameterException("AnsiColor is not an ansi sequence");

        for (int i = 0; i < height; i++) {

            for (int j = 0; j < width; j++) {

                if (!chars[i][j].contains("\0")) {

                    if (chars[i][j].startsWith(String.valueOf(ESCAPE_CHAR))) chars[i][j] = ansiColor + chars[i][j].substring(chars[i][j].indexOf('m') + 1);
                    else chars[i][j] = ansiColor + chars[i][j];

                    if (!chars[i][j].endsWith(RESET)) chars[i][j] = chars[i][j] + RESET;
                }
            }
        }
    }

    @Override
    public void setHidden(boolean b) {

        this.hidden = b;
    }

    @Override
    public void printToFrame(Frame frame) {

        if(!hidden) {

            for (int i = 0; i < height; i++) {

                for (int j = 0; j < width; j++) {

                    if (!chars[i][j].contains("\0") && y + i >= 0 && y + i < frame.getHeight() && x + j >= 0 && x + j < frame.getWidth())
                        frame.setChar(getFrameX() + j, getFrameY() + i, chars[i][j]);
                }
            }
        }
    }

    @Override
    public int getFrameX() {

        return (int) x;
    }

    @Override
    public float getX() {

        return x;
    }

    @Override
    public void setX(float x) {

        this.x = x;
    }

    @Override
    public int getFrameY() {

        return (int) y;
    }

    @Override
    public float getY() {

        return y;
    }

    @Override
    public void setY(float y) {

        this.y = y;
    }

    @Override
    public void setPosition(float x, float y) {

        setX(x);
        setY(y);
    }

    @Override
    public int getWidth() {

        return width;
    }

    @Override
    public int getHeight() {

        return height;
    }
}
