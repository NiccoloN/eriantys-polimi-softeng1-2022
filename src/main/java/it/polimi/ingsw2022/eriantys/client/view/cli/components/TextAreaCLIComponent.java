package it.polimi.ingsw2022.eriantys.client.view.cli.components;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw2022.eriantys.client.view.cli.AnsiCodes.*;

/**
 * This class represents a cli component that contains text. Text can be animated to write at a certain speed (and it is by default)
 * @author Niccolò Nicolosi
 */
public class TextAreaCLIComponent extends AnimatedCLIComponent {

    public static final String DEFAULT_COLOR = RESET;
    public static final String TEXT_DEFAULT_COLOR = BLACK + WHITE_BACKGROUND_BRIGHT;

    private String color;
    private String textColor;
    private final String label;
    private final List<String> textRows;
    private final int maxTextWidth, maxTextHeight;
    private float textSpeed;
    private int printedChars;

    /**
     * Constructs a text area cli component of the given size with no label
     * @param width the width of the component
     * @param height the height of the component
     */
    public TextAreaCLIComponent(int width, int height) {

        this(width, height, "");
    }

    /**
     * Constructs a text area cli component of the given size and with a given label
     * @param width the width of the component
     * @param height the height of the component
     * @param label the label of the text area
     */
    public TextAreaCLIComponent(int width, int height, String label) {

        super(width, height);
        if (label.length() > width - 4) throw new InvalidParameterException("Label too long for this text area");

        color = DEFAULT_COLOR;
        textColor = TEXT_DEFAULT_COLOR;

        this.label = label;

        maxTextWidth = width - 2;
        maxTextHeight = height - 3;
        textRows = new ArrayList<>(maxTextHeight);
        for(int n = 0; n < maxTextHeight; n++) textRows.add("");

        textSpeed = 180;

        buildRows();
    }

    private void buildRows() {

        setRow(0, color + " _" + label + "_".repeat(getWidth() - label.length() - 3) + " " + RESET);
        setRow(1, color + "|" + " ".repeat(getWidth() - 2) + "|" + RESET);

        printedChars = 0;
        for(int n = 2; n < getHeight() - 1; n++) setRow(n, buildTextRow(n - 2));

        setRow(getHeight() - 1, color + "|" + "_".repeat(getWidth() - 2) + "|" + RESET);
    }

    private String buildTextRow(int textRowIndex) {

        StringBuilder row = new StringBuilder();
        row.append(color).append("|").append(textColor);

        String text = textRows.get(textRowIndex);
        text = ansiTextSubstring(text, 0,
                Math.min(Math.max((int) (textSpeed * getStateTime()) - printedChars, 0), noAnsiString(text).length()));
        printedChars += noAnsiString(text).length();
        row.append(text).append(" ".repeat(getWidth() - noAnsiString(text).length() - 2));
        row.append(RESET).append(color).append("|").append(RESET);

        return row.toString();
    }

    @Override
    protected void update() {

        super.update();
        buildRows();
    }

    @Override
    public void setColor(String ansiColor) {

        this.color = ansiColor;
    }

    public void setTextColor(String textColor) {

        this.textColor = textColor;
    }

    /**
     * Sets the text to be written in this text area
     * @param text the text to be written
     */
    public void setText(String text) {

        int i = 0;
        String[] words = text.split(" ");

        StringBuilder row;
        int nlIndex;

        for (int n = 0; n < textRows.size(); n++) {

            row = new StringBuilder();
            while (i < words.length && noAnsiString(row + words[i] + " ").length() - 1 <= maxTextWidth) {

                if (words[i].contains("\n")) {

                    nlIndex = words[i].indexOf("\n");
                    row.append(words[i], 0, nlIndex).append(" ");
                    words[i] = words[i].substring(nlIndex + 1);
                    break;
                }
                else {

                    row.append(words[i]).append(" ");
                    i++;
                }
            }

            textRows.set(n, row.toString().stripTrailing());
        }

        reset();
    }

    /**
     * @return the writing speed of this text area
     */
    public float getTextSpeed() {

        return textSpeed;
    }

    /**
     * Sets the writing speed of this text area
     * @param textSpeed the new writing speed
     */
    public void setTextSpeed(float textSpeed) {

        this.textSpeed = textSpeed;
    }
}
