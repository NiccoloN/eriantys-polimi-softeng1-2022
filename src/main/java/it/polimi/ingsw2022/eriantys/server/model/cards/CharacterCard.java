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
    private final int initialCost;
    private int cost;

    private final List<ColoredPawn> students;
    private int denyTilesNumber;

    CharacterCard(int index, String effect, int cost) {

        super(index);
        this.effect = effect;
        this.cost = cost;
        initialCost = cost;

        students = new ArrayList<>(4);
        denyTilesNumber = 0;
    }

    public int getCost() {
        return cost;
    }

    public void incrementCost() {
        if(initialCost == cost) this.cost = this.cost + 1;
    }

    public int getStudentsNumber() {
        return students.size();
    }

    public ColoredPawn getStudent(int index) {
        return students.get(index);
    }

    public ColoredPawn getStudent(PawnColor color) {

        return students.stream().filter((x) -> x.color == color).findAny().orElseThrow();
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

    public int getDenyTilesNumber() {
        return denyTilesNumber;
    }

    public void decrementDenyTiles() {

        if (denyTilesNumber == 0) throw new RuntimeException("Number of deny tiles can't be negative");
        this.denyTilesNumber--;
    }

    public void incrementDenyTiles() {
        if (denyTilesNumber == 4) throw new RuntimeException("Number of deny tiles can't be greater than 4");
        this.denyTilesNumber++;
    }
}
