package it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.components;

import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.components.BasicCLIComponent;

import java.security.InvalidParameterException;

import static it.polimi.ingsw2022.eriantys.client.view.cli.AnsiCodes.RED;
import static it.polimi.ingsw2022.eriantys.client.view.cli.AnsiCodes.RESET;

/**
 * This class represents a helper card cli component
 *
 * @author Niccolò Nicolosi
 */
public class HelperCardCLIComponent extends BasicCLIComponent {
    
    public static final int WIDTH = 7, HEIGHT = 5;
    
    private final int index;
    private final int priority, movement;
    private boolean playable;
    
    /**
     * Constructs a helper card cli component with the given index, priority value and movement value
     *
     * @param index    the index of the helper card associated to this component
     * @param priority the priority value of the helper card associated to this component
     * @param movement the movement value of the helper card associated to this component
     * @throws InvalidParameterException if index < 1 or > 10,
     *                                   if priority < 1 or > 99,
     *                                   if movement < 1 or > 99
     */
    public HelperCardCLIComponent(int index, int priority, int movement) {
        
        super(WIDTH, HEIGHT);
        
        if (index < 0 || index > 10) throw new InvalidParameterException("Index must be between 1 and 10");
        this.index = index;
        
        if (priority < 0 || priority > 99) throw new InvalidParameterException("Priority must be between 1 and 99");
        this.priority = priority;
        
        if (movement < 0 || movement > 99) throw new InvalidParameterException("Movement must be between 1 and 99");
        this.movement = movement;
        
        playable = true;
        
        buildRows();
    }
    
    /**
     * @return the index of the helper card associated to this component
     */
    public int getIndex() {
        
        return index;
    }
    
    /**
     * @return whether this helper card component is visualized as playable
     */
    public boolean isPlayable() {
        
        return playable;
    }
    
    /**
     * Sets this helper card component to be visualized as playable or not
     */
    public void setPlayable(boolean playable) {
        
        this.playable = playable;
        setColor(playable ? RESET : RED);
    }
    
    private void buildRows() {
        
        setRow(0, " _____ ");
        setRow(1, "|" + String.format("%02d", priority) + " " + String.format("%02d", movement) + "|");
        setRow(2, "|  o  |");
        setRow(3, "| (_) |");
        setRow(4, "|_____|");
    }
}
