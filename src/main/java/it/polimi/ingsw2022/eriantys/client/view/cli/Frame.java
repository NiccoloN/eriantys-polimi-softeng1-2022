package it.polimi.ingsw2022.eriantys.client.view.cli;

import java.security.InvalidParameterException;
import java.util.Arrays;

/**
 * This class represents a frame of the cli. A frame is a bi-dimensional array of ascii characters, each of which can have ansi properties.
 * Therefore, a char is represented by an ansi string. A frame can be converted to a string to be printed to the terminal window.
 * To correctly visualize a frame in the terminal, the terminal window must be of a size greater or equal than the size of the frame
 *
 * @author Niccolò Nicolosi
 */
public class Frame {
    
    private final String[][] chars;
    
    /**
     * Constructs a frame of the given size
     *
     * @param width  the width of the frame
     * @param height the height of the frame
     */
    public Frame(int width, int height) {
        
        chars = new String[height][width];
    }
    
    /**
     * Sets the character of this frame at position (x, y) to the given character (y-axis points downwards)
     *
     * @param x         the x position of the character to set
     * @param y         the y position of the character to set
     * @param character the new character (can have ansi properties)
     * @throws InvalidParameterException if the given character is not valid
     */
    public void setChar(int x, int y, String character) {
        
        int noAnsiLength = AnsiCodes.noAnsiString(character).length();
        if (noAnsiLength < 1) throw new InvalidParameterException("Character only contains ansi sequences");
        if (noAnsiLength > 1) throw new InvalidParameterException("Character contains more than 1 character");
        chars[y][x] = character;
    }
    
    /**
     * @param terminalWidth  the width of the terminal this string will be printed to
     * @param terminalHeight the height of the terminal this string will be printed to
     * @return this frame converted to a single ansi string to print to the terminal window.
     * To correctly visualize the frame, the terminal window must be at least of the size of the frame
     * @throws InvalidParameterException if the give terminal size is smaller than the size of this frame
     */
    public String getAnsiString(int terminalWidth, int terminalHeight) {
        
        if (terminalWidth < getWidth() || terminalHeight < getHeight())
            throw new InvalidParameterException("Cannot build a string from this frame that matches the given terminal size");
        
        int paddingTop = (int) Math.floor((terminalHeight - getHeight()) / 2f);
        int paddingBottom = (int) Math.ceil((terminalHeight - getHeight()) / 2f);
        int paddingRight = (int) Math.ceil((terminalWidth - getWidth()) / 2f);
        int paddingLeft = (int) Math.floor((terminalWidth - getWidth()) / 2f);
        
        StringBuilder stringBuilder = new StringBuilder(terminalWidth * terminalHeight);
        
        for (int n = 0; n < paddingTop; n++) stringBuilder.append(" ".repeat(terminalWidth)).append("\n");
        
        for (int i = 0; i < getHeight(); i++) {
            
            stringBuilder.append(" ".repeat(paddingLeft));
            for (int j = 0; j < getWidth(); j++) stringBuilder.append(chars[i][j]);
            stringBuilder.append(" ".repeat(paddingRight));
            if (paddingTop + i != terminalHeight - 1) stringBuilder.append("\n");
        }
        
        for (int n = 0; n < paddingBottom; n++) {
            
            stringBuilder.append(" ".repeat(terminalWidth));
            if (paddingTop + getHeight() + n != terminalHeight - 1) stringBuilder.append("\n");
        }
        
        return stringBuilder.toString();
    }
    
    public int getWidth() {
        
        return chars[0].length;
    }
    
    public int getHeight() {
        
        return chars.length;
    }
    
    /**
     * Clears this frame, filling every cell with a space
     */
    public void clear() {
        
        for (String[] row : chars) Arrays.fill(row, " ");
    }
}