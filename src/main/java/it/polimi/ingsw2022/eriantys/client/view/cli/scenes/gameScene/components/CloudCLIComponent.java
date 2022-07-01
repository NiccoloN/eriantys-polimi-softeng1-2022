package it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.components;

import it.polimi.ingsw2022.eriantys.client.view.cli.Frame;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.components.BasicCLIComponent;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

import static it.polimi.ingsw2022.eriantys.client.view.cli.AnsiCodes.CYAN;
import static it.polimi.ingsw2022.eriantys.client.view.cli.AnsiCodes.RESET;

/**
 * This class represents a cloud cli component
 * @author Niccolò Nicolosi
 */
public class CloudCLIComponent extends BasicCLIComponent {
    
    public static final int WIDTH = 9, HEIGHT = 5;
    public static final String DEFAULT_COLOR = CYAN;
    private final int index;
    private final Map<PawnColor, Integer> students;
    private String color;
    
    /**
     * Constructs a cloud cli component with the given index
     * @param index the index of this cloud
     * @throws InvalidParameterException if index is < 0 or > 9
     */
    public CloudCLIComponent(int index) {
        
        super(WIDTH, HEIGHT);
        
        color = DEFAULT_COLOR;
        
        if (index < 0 || index > 9) throw new InvalidParameterException("Index must be >= 0 and <= 9");
        this.index = index;
        
        students = new HashMap<>(5);
        for (PawnColor color : PawnColor.values()) students.put(color, 0);
        
        buildRows();
    }
    
    @Override
    public void printToFrame(Frame frame) {
        
        buildRows();
        super.printToFrame(frame);
    }
    
    @Override
    public void setColor(String ansiColor) {
        
        color = ansiColor;
    }
    
    /**
     * @return the index of the cloud associated to this component
     */
    public int getIndex() {
        
        return index;
    }
    
    /**
     * Sets the students of the given color to visualize on this cloud component
     * @param color    the color of the students
     * @param students the students of students to visualize
     * @throws InvalidParameterException if students is not between 0 and 9
     */
    public void setStudents(PawnColor color, int students) {
        
        if (students < 0 || students > 9) throw new InvalidParameterException("Students must be >= 0 and <= 9");
        this.students.put(color, students);
    }
    
    private void buildRows() {
        
        int red = students.get(PawnColor.RED);
        int green = students.get(PawnColor.GREEN);
        int yellow = students.get(PawnColor.YELLOW);
        int blue = students.get(PawnColor.BLUE);
        int pink = students.get(PawnColor.PINK);
        
        setRow(0, color + "  _____  " + RESET);
        setRow(1, color + " (     ) " + RESET);
        setRow(2, color + "( " + PawnColor.RED.ansiForegroundColor + (red == 0 ? " " : red) + PawnColor.GREEN.ansiForegroundColor + (green == 0 ? " " : green) + PawnColor.YELLOW.ansiForegroundColor + (yellow == 0 ? " " : yellow) + PawnColor.BLUE.ansiForegroundColor + (blue == 0 ? " " : blue) + PawnColor.PINK.ansiForegroundColor + (pink == 0 ? " " : pink) + color + " )" + RESET);
        setRow(3, color + " (_____) " + RESET);
        setRow(4, RESET + "    " + String.format("%01d", index) + "    ");
    }
}
