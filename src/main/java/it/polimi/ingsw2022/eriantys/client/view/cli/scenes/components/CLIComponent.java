package it.polimi.ingsw2022.eriantys.client.view.cli.scenes.components;

import it.polimi.ingsw2022.eriantys.client.view.cli.Frame;

import java.security.InvalidParameterException;

/**
 * This class represents a CLI component. A CLI component is a bi-dimensional graphic element that can be printed to a frame at a specific position
 * @see Frame
 * @author Niccolò Nicolosi
 */
public interface CLIComponent {

    /**
     * Prints this component to the given frame at its position, if not hidden
     * @param frame the frame to print to
     */
    void printToFrame(Frame frame);

    /**
     * @return the x position of this component on the frame (integer value)
     */
    int getFrameX();

    float getX();

    void setX(float x);

    /**
     * @return the y position of this component on the frame (integer value)
     */
    int getFrameY();

    float getY();

    void setY(float y);

    void setPosition(float x, float y);

    int getWidth();

    int getHeight();

    /**
     * Sets the color of this component
     * @param ansiColor the new color
     * @throws InvalidParameterException if the given color is not an ansi sequence
     */
    void setColor(String ansiColor);

    /**
     * Sets whether this component is hidden or not
     * @param b true to hide the component, false to make it visible
     */
    void setHidden(boolean b);
}
