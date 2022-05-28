package it.polimi.ingsw2022.eriantys.server.model.cards;

import it.polimi.ingsw2022.eriantys.server.model.pawns.ColoredPawn;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;

import java.util.ArrayList;
import java.util.List;

/**
 * Special card with an associated Skill
 * @author Francesco Melegati Maccari
 */
public class CharacterCard extends Card {

    public final String effect;
    private int cost;

    private final List<ColoredPawn> students;
    private ColoredPawn pawnColor;
    private int counter;

    CharacterCard(int index, String effect, int cost) {

        super(index);
        this.effect = effect;
        this.cost = cost;

        students = new ArrayList<>(4);
        pawnColor = null;
        counter = 0;
    }

    public int getCost() {
        return cost;
    }

    public void incrementCost() {
        this.cost = this.cost + 1;
    }

    public ColoredPawn getPawnColor() {
        return pawnColor;
    }

    public void setPawnColor(ColoredPawn pawnColor) {
        this.pawnColor = pawnColor;
    }

    public int getStudentsNumber() {
        return students.size();
    }

    public ColoredPawn getStudent(int index) {
        return students.get(index);
    }

    public void addStudent(ColoredPawn student) {
        students.add(student);
    }

    public void removeStudent(ColoredPawn student) {
        students.remove(student);
    }

    public List<PawnColor> getStudentsColors() {

        ArrayList<PawnColor> availableColors = new ArrayList<>();
        for(PawnColor color : PawnColor.values())
            if(students.stream().anyMatch((x) -> x.color == color)) availableColors.add(color);

        return availableColors;
    }

    public int getCounter() {
        return counter;
    }

    public void decrementCounter() {

        if (counter == 0) throw new RuntimeException("Counter has a negative value");
        this.counter--;
    }
}
