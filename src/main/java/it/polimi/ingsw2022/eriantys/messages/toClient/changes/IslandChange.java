package it.polimi.ingsw2022.eriantys.messages.toClient.changes;

import java.util.OptionalInt;

public class IslandChange implements Change {

    private final int islandIndex;
    private OptionalInt red;

    public IslandChange(int islandIndex) {

        this.islandIndex = islandIndex;
        red = OptionalInt.of(4);
    }

    @Override
    public void apply() {


    }
}
