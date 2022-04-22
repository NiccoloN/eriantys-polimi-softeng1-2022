package it.polimi.ingsw2022.eriantys.view.cli.components;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw2022.eriantys.view.cli.components.AnsiColorCodes.*;

public class TextAreaCLIComponent extends CLIComponent {

    private static final String TEXT_AREA_DEFAULT_COLOR = RESET;
    private static final String TEXT_DEFAULT_COLOR = BLACK + WHITE_BACKGROUND_BRIGHT;

    private String color;
    private String textColor;
    private final String label;
    private final List<String> textRows;
    private final int maxTextWidth, maxTextHeight;

    public TextAreaCLIComponent(int width, int height, String label) {

        super(width, height);
        if (label.length() > width - 4) throw new InvalidParameterException("Label too long for this text area");

        color = TEXT_AREA_DEFAULT_COLOR;
        textColor = TEXT_DEFAULT_COLOR;

        this.label = label;

        maxTextWidth = width - 2;
        maxTextHeight = height - 3;
        textRows = new ArrayList<>(maxTextHeight);
        for(int n = 0; n < maxTextHeight; n++) textRows.add("");

        buildRows();
    }

    private void buildRows() {

        setRow(0, color + " _" + label + "_".repeat(getWidth() - label.length() - 3) + " " + RESET);
        setRow(1, color + "|" + " ".repeat(getWidth() - 2) + "|" + RESET);
        for(int n = 2; n < getHeight() - 1; n++)
            setRow(n, color + "|" + textColor + textRows.get(n - 2) + " ".repeat(getWidth() - textRows.get(n - 2).length() - 2) + RESET + color + "|" + RESET);
        setRow(getHeight() - 1, color + "|" + "_".repeat(getWidth() - 2) + "|" + RESET);
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

    public void setTextColor(String textColor) {

        this.textColor = textColor;
    }

    public void setText(String text) {

        int i = 0;
        String[] words = text.split(" ");

        StringBuilder row;

        for (int n = 0; n < textRows.size(); n++) {

            row = new StringBuilder();
            while (i < words.length && noAnsiLength(row + words[i] + " ") - 1 <= maxTextWidth) {

                row.append(words[i]).append(" ");
                i++;
            }

            textRows.set(n, row.toString().stripTrailing());
        }
    }

    private int noAnsiLength(String ansiString) {

        char[] chars = ansiString.toCharArray();

        int length = 0;
        boolean toConsider = true;

        for (int n = 0; n < chars.length; n++) {

            if (chars[n] == 27) toConsider = false;
            if (toConsider) length++;
            if (chars[n] == 'm') toConsider = true;
        }

        return length;
    }
}
