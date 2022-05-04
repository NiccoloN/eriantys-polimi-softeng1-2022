package it.polimi.ingsw2022.eriantys.messages.toClient.changes;

import it.polimi.ingsw2022.eriantys.client.view.View;

import java.util.ArrayList;
import java.util.List;

public class Update {
    private List<Change> changes;

    public Update() {
        this.changes = new ArrayList<>(1);
    }

    public void addChange(Change change) {
        changes.add(change);
    }

    private void applyChanges(View view) {
        for (Change change : changes) {
            change.apply(view);
        }
    }
}
