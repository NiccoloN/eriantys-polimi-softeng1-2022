package it.polimi.ingsw2022.eriantys.client.view.cli.scenes.components;

import it.polimi.ingsw2022.eriantys.client.view.cli.Frame;

import java.security.InvalidParameterException;

/**
 * This class represents an animated cli component
 *
 * @author Niccolò Nicolosi
 */
public class AnimatedCLIComponent extends BasicCLIComponent {
    
    protected float speedX, speedY;
    private float stateTime, deltaTime;
    private long lastUpdateTime;
    
    /**
     * Constructs an animated cli component from the given rows
     *
     * @param width the width of the component
     * @param rows  the rows from which to construct the component
     * @throws InvalidParameterException if any of the given rows is not of the given width
     */
    public AnimatedCLIComponent(int width, String[] rows) {
        
        this(width, rows.length);
        for (int n = 0; n < rows.length; n++) setRow(n, rows[n]);
    }
    
    /**
     * Constructs an empty animated cli component of the given size
     *
     * @param width  the width of the component
     * @param height the height of the component
     * @throws InvalidParameterException if width or height are < 1
     */
    protected AnimatedCLIComponent(int width, int height) {
        
        super(width, height);
        reset();
        speedX = 0;
        speedY = 0;
    }
    
    /**
     * Resets the state (and so the animation) of this component
     */
    public void reset() {
        
        stateTime = 0;
        deltaTime = 0;
        lastUpdateTime = -1;
    }
    
    @Override
    public void printToFrame(Frame frame) {
        
        update();
        super.printToFrame(frame);
    }
    
    /**
     * @return the time passed since the start of the animation in seconds
     */
    public float getStateTime() {
        
        return stateTime;
    }
    
    /**
     * @return the time passed since the last update in seconds
     */
    public float getDeltaTime() {
        
        return deltaTime;
    }
    
    /**
     * @return the movement speed of this component on the x-axis
     */
    public float getSpeedX() {
        
        return speedX;
    }
    
    /**
     * Sets the movement speed of this component on the x-axis
     *
     * @param speedX the new movement speed
     */
    public void setSpeedX(float speedX) {
        
        this.speedX = speedX;
    }
    
    /**
     * @return the movement speed of this component on the y-axis
     */
    public float getSpeedY() {
        
        return speedY;
    }
    
    /**
     * Sets the movement speed of this component on the y-axis
     *
     * @param speedY the new movement speed
     */
    public void setSpeedY(float speedY) {
        
        this.speedY = speedY;
    }
    
    /**
     * Updates the component
     */
    protected void update() {
        
        if (lastUpdateTime >= 0) deltaTime = (System.nanoTime() - lastUpdateTime) / 1000000000f;
        stateTime += deltaTime;
        lastUpdateTime = System.nanoTime();
        setX(getX() + speedX * deltaTime);
        setY(getY() + speedY * deltaTime);
    }
}
