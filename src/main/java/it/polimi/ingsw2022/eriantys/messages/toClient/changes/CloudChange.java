package it.polimi.ingsw2022.eriantys.messages.toClient.changes;

import it.polimi.ingsw2022.eriantys.client.view.View;
import it.polimi.ingsw2022.eriantys.server.model.board.CloudTile;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;

import java.io.Serializable;

public class CloudChange implements Change, Serializable {

    public final Integer cloudIndex;
    private final CloudTile cloud;

    public CloudChange(Integer cloudIndex, CloudTile cloud) {

        this.cloudIndex = cloudIndex;
        this.cloud = cloud;
    }

    public int getStudents(PawnColor color) {

        return cloud.countStudents(color);
    }

    @Override
    public void apply(View view) {

        view.applyChange(this);
    }
}
