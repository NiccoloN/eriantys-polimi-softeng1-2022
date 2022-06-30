package it.polimi.ingsw2022.eriantys.client.view.cli.scenes.components;

import java.security.InvalidParameterException;

import static it.polimi.ingsw2022.eriantys.client.view.cli.AnsiCodes.BLACK;
import static it.polimi.ingsw2022.eriantys.client.view.cli.AnsiCodes.WHITE_BRIGHT;

/**
 * This class represents a blinking cli component
 *
 * @author Niccolò Nicolosi
 */
public class BlinkingCLIComponent extends AnimatedCLIComponent {
    
    private enum BlinkState {
        
        FIRST, SECOND
    }
    
    private float blinkTime;
    private BlinkState state;
    private String firstColor, secondColor;
    
    /**
     * Constructs a blinking cli component from the given rows
     *
     * @param width the width of the component
     * @param rows  the rows from which to construct the component
     * @throws InvalidParameterException if any of the given rows is not of the given width
     */
    public BlinkingCLIComponent(int width, String[] rows) {
        
        this(width, rows.length);
        for (int n = 0; n < rows.length; n++) setRow(n, rows[n]);
    }
    
    /**
     * Constructs an empty blinking cli component of the given size
     *
     * @param width  the width of the component
     * @param height the height of the component
     * @throws InvalidParameterException if width or height are < 1
     */
    public BlinkingCLIComponent(int width, int height) {
        
        super(width, height);
        firstColor = WHITE_BRIGHT;
        secondColor = BLACK;
        blinkTime = 0.5f;
        
        setColor(firstColor);
        state = BlinkState.FIRST;
    }
    
    /**
     * Sets the blink time of this component
     *
     * @param blinkTime the new blink time
     */
    public void setBlinkTime(float blinkTime) {
        
        this.blinkTime = blinkTime;
    }
    
    /**
     * Sets the first blink color
     *
     * @param firstColor the new blink color
     */
    public void setFirstColor(String firstColor) {
        
        this.firstColor = firstColor;
        if (state == BlinkState.FIRST) setColor(firstColor);
    }
    
    /**
     * Sets the second blink color
     *
     * @param secondColor the new blink color
     */
    public void setSecondColor(String secondColor) {
        
        this.secondColor = secondColor;
        if (state == BlinkState.SECOND) setColor(secondColor);
    }
    
    @Override
    protected void update() {
        
        super.update();
        if (getStateTime() < blinkTime && state == BlinkState.SECOND) {
            
            setColor(firstColor);
            state = BlinkState.FIRST;
        }
        else if (getStateTime() >= blinkTime && state == BlinkState.FIRST) {
            
            setColor(secondColor);
            state = BlinkState.SECOND;
        }
        if (getStateTime() > blinkTime * 2) reset();
    }
}
