package it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.components;

import it.polimi.ingsw2022.eriantys.client.view.cli.Frame;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.components.BasicCLIComponent;
import it.polimi.ingsw2022.eriantys.server.model.cards.CharacterCard;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

import static it.polimi.ingsw2022.eriantys.client.view.cli.AnsiCodes.*;

/**
 * This class represents a character card cli component
 * @author Niccolò Nicolosi
 */
public class CharacterCardCLIComponent extends BasicCLIComponent {
    
    public static final int WIDTH = 9, HEIGHT = 6;
    public static final String CHARACTER_DEFAULT_COLOR = RESET;
    private final int index;
    private final String effect;
    private final int denyTiles;
    private final Map<PawnColor, Integer> students;
    private String color;
    private int cost;
    
    /**
     * Constructs a character card cli component from the given character card
     * @param card the character card from which to construct this component
     * @throws InvalidParameterException if the given character card is not valid
     */
    public CharacterCardCLIComponent(CharacterCard card) {
        
        super(WIDTH, HEIGHT);
        
        color = CHARACTER_DEFAULT_COLOR;
        index = card.index;
        effect = card.effect;
        cost = card.getCost();
        
        if (card.getDenyTilesNumber() > 4) throw new InvalidParameterException("Deny tiles number must be <= 4");
        denyTiles = card.getDenyTilesNumber();
        
        students = new HashMap<>();
        for (PawnColor color : PawnColor.values()) setStudents(color, card.countStudents(color));
    }
    
    @Override
    public void printToFrame(Frame frame) {
        
        buildRows();
        super.printToFrame(frame);
    }
    
    @Override
    public void setColor(String ansiColor) {
        
        this.color = ansiColor;
    }
    
    /**
     * @return the index of the character card associate to this component
     */
    public int getIndex() {
        
        return index;
    }
    
    /**
     * @return the text that describes the effect of the character card associate to this component
     */
    public String getEffect() {
        
        return effect;
    }
    
    /**
     * @return the cost of the character card associate to this component
     */
    public int getCost() {
        
        return cost;
    }
    
    /**
     * Sets the cost to visualize on this card component
     * @param cost the new cost
     */
    public void setCost(int cost) {
        
        if (cost < 0 || cost > 99) throw new InvalidParameterException("Cost must be between 1 and 99");
        this.cost = cost;
    }
    
    /**
     * Sets the students of the given color to visualize on this card
     * @param color  the color of the students
     * @param number the number of students to visualize
     * @throws InvalidParameterException if number is not between 0 and 9
     */
    private void setStudents(PawnColor color, int number) {
        
        if (number < 0 || number > 9) throw new InvalidParameterException("Number must be >= 0 and <= 9");
        students.put(color, number);
    }
    
    private void buildRows() {
        
        setRow(0, color + " _______ " + RESET);
        setRow(1, color + "| " + YELLOW + "C" + RESET + color + ": " + (cost < 10 ? "0" : " ") + cost + " |" + RESET);
        setRow(2, color + "|   o   |" + RESET);
        setRow(3, color + "|__(_)__|" + RESET);
        
        boolean anyStudent = false;
        for (PawnColor color : PawnColor.values()) {
            
            if (students.get(color) > 0) {
                
                anyStudent = true;
                break;
            }
        }
        
        if (anyStudent) {
            
            int red = students.get(PawnColor.RED);
            int green = students.get(PawnColor.GREEN);
            int yellow = students.get(PawnColor.YELLOW);
            int blue = students.get(PawnColor.BLUE);
            int pink = students.get(PawnColor.PINK);
            
            setRow(4, color + "| " + PawnColor.RED.ansiForegroundColor + (red == 0 ? " " : red) + PawnColor.GREEN.ansiForegroundColor + (green == 0 ? " " : green) + PawnColor.YELLOW.ansiForegroundColor + (yellow == 0 ? " " : yellow) + PawnColor.BLUE.ansiForegroundColor + (blue == 0 ? " " : blue) + PawnColor.PINK.ansiForegroundColor + (pink == 0 ? " " : pink) + RESET + color + " |" + RESET);
        }
        else
            setRow(4, color + "| " + RED_BRIGHT + (denyTiles > 0 ? "X" : " ") + (denyTiles > 1 ? "X" : " ") + " " + (denyTiles > 2 ? "X" : " ") + (denyTiles > 3 ? "X" : " ") + color + " |" + RESET);
        
        setRow(5, color + "|_______|" + RESET);
    }
}
