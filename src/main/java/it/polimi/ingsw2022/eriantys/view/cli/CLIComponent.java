package it.polimi.ingsw2022.eriantys.view.cli;

import java.util.Arrays;

import static it.polimi.ingsw2022.eriantys.view.cli.AnsiColorCodes.*;

public class CLIComponent {

    private int x, y;
    private final int width, height;
    private final String[] rows;

    public CLIComponent(int width, String[] rows) {

        x = 0;
        y = 0;
        this.width = width;
        this.height = rows.length;
        this.rows = rows;
    }

    public CLIComponent(int width, int height) {

        this(width, new String[height]);
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

    protected String getRow(int index) {

        return rows[index];
    }

    protected void setRow(int index, String row) {

        rows[index] = row;
    }

    public void setColor(String ansiColor) {

        if (rows[0].startsWith(ESCAPE)) rows[0] = ansiColor + rows[0].substring(rows[0].indexOf('m') + 1);
        else rows[0] = ansiColor + rows[0];

        if (!rows[rows.length - 1].endsWith(RESET)) rows[rows.length - 1] = rows[rows.length - 1] + RESET;
    }
}
