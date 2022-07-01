package it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.utilityNodes;

import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.Images;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;

/**
 * This class represents SizedImageView associated with a specific PawnColor and set to have a specific image representing
 * a student or a professor
 * @author Emanuele Musto
 * @see SizedImageView
 * @see PawnColor
 */
public class ColoredPawnImageView extends SizedImageView {
    
    PawnColor color;
    
    /**
     * Constructs a colored pawn image view of the given size
     * @param size the size of the image view
     */
    public ColoredPawnImageView(int size) {
        
        super(size);
        color = null;
    }
    
    /**
     * @return the color associated to this image view
     */
    public PawnColor getColor() {
        
        return color;
    }
    
    /**
     * Sets the image of this view to represent a student of the given color and associate this view to the given color
     * @param color the color of the student to represent
     */
    public void setStudentOfColor(PawnColor color) {
        
        this.color = color;
        setImage(Images.studentsImages.get(color));
        setVisible(true);
    }
    
    /**
     * Sets the image of this view to represent a professor of the given color and associate this view to the given color
     * @param color the color of the professor to represent
     */
    public void setProfessorOfColor(PawnColor color) {
        
        this.color = color;
        setImage(Images.professorsImages.get(color));
        setVisible(true);
    }
    
    /**
     * Remove the previous association of this view with a color and sets this view as not visible
     */
    public void clearColor() {
        
        this.color = null;
        setVisible(false);
    }
}
