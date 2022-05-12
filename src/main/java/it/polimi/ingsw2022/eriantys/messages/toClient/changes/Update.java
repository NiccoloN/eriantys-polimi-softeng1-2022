package it.polimi.ingsw2022.eriantys.messages.toClient.changes;

import it.polimi.ingsw2022.eriantys.client.view.View;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Francesco Melegati Maccari
 * @author Niccolò Nicolosi
 * @author Emanuele Musto
 */
public class Update implements Serializable {

    private final List<Change> changes;

    public Update() {
        this.changes = new ArrayList<>(1);
    }

    public void addChange(Change change) {
        changes.add(change);
    }

    public void applyChanges(View view) {

        for (Change change : changes) change.apply(view);
    }
}
