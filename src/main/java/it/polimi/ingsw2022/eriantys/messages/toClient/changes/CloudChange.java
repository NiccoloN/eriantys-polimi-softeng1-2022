package it.polimi.ingsw2022.eriantys.messages.toClient.changes;

import it.polimi.ingsw2022.eriantys.client.view.View;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CloudChange implements Change, Serializable {

    private List<PawnColor> students;
    private Integer cloudIndex;

    // Constructor to create filled cloud
    public CloudChange(Integer cloudIndex, List<PawnColor> students) {
        this.students = students;
        this.cloudIndex = cloudIndex;
    };

    // Constructor to create empty cloud
    public CloudChange(Integer cloudIndex) {
        this.cloudIndex = cloudIndex;
        this.students = new ArrayList<>(0);
    }

    @Override
    public void apply(View view) {
        view.applyChange(this);
    }
}
