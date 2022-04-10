package it.polimi.ingsw2022.eriantys.model.cards;

import it.polimi.ingsw2022.eriantys.model.board.CloudTile;
import it.polimi.ingsw2022.eriantys.model.board.CompoundIslandTile;
import it.polimi.ingsw2022.eriantys.model.board.SchoolDashboard;
import it.polimi.ingsw2022.eriantys.model.pawns.ColoredPawn;

import java.util.ArrayList;
import java.util.List;


/**
 * Extension of the CharacterCard used for special skill that needs to have students pawns placed on card
 * @author Francesco Melegati Maccari
 */
public class StudentsListCharacterCard extends CharacterCard{
    private final List<ColoredPawn> students;

    /**
     * @param index  index of the card
     * @param skill skill that needs to be handled by the card
     */
    StudentsListCharacterCard(int index, Skill skill) {
        super(index, skill);
        this.students = new ArrayList<>();
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
}
