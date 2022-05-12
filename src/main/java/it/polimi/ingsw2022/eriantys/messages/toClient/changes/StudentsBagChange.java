package it.polimi.ingsw2022.eriantys.messages.toClient.changes;

import it.polimi.ingsw2022.eriantys.client.view.View;

import java.io.Serializable;

public class StudentsBagChange implements Change, Serializable {

    private int numberOfStudents;

    public StudentsBagChange(int numberOfStudents) {
        this.numberOfStudents = numberOfStudents;
    }

    @Override
    public void apply(View view) {
    view.applyChange(this);
    }
}
