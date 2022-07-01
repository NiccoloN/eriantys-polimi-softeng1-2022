package it.polimi.ingsw2022.eriantys.server.model.cards;

import it.polimi.ingsw2022.eriantys.server.model.pawns.ColoredPawn;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CharacterCardTest {

    @Test
    void testGetCost() {

        CharacterCard characterCard = new CharacterCard(1, "ciao", 2);
        assertEquals(characterCard.getCost(), 2);
    }

    @Test
    void testIncrementCost() {

        CharacterCard characterCard = new CharacterCard(1, "ciao", 2);
        assertEquals(characterCard.getCost(), 2);
        assertFalse(characterCard.isCostIncremented());
        characterCard.incrementCost();
        assertEquals(characterCard.getCost(), 3);
        assertTrue(characterCard.isCostIncremented());
    }

    @Test
    void testStudents() {

        CharacterCard characterCard = new CharacterCard(1, "ciao", 2);
        for(int i = 0; i < 2; i++) characterCard.addStudent(new ColoredPawn(PawnColor.RED));
        for(int i = 0; i < 2; i++) characterCard.addStudent(new ColoredPawn(PawnColor.YELLOW));

        assertEquals(characterCard.countStudents(PawnColor.RED), 2);
        assertEquals(characterCard.countStudents(PawnColor.BLUE), 0);

        assertEquals(characterCard.getStudentsColors(), List.of(PawnColor.RED, PawnColor.YELLOW));

        ColoredPawn test = new ColoredPawn(PawnColor.GREEN);
        characterCard.addStudent(test);
        assertEquals(characterCard.getStudent(PawnColor.GREEN), test);

        assertEquals(characterCard.countStudents(PawnColor.GREEN), 1);

        characterCard.removeStudent(test);
        assertEquals(characterCard.countStudents(PawnColor.GREEN), 0);
    }

    @Test
    void testDenyCards() {

        CharacterCard characterCard = new CharacterCard(1, "ciao", 2);
        for(int i = 0; i < 5; i++) characterCard.incrementDenyTiles();
        assertEquals(characterCard.getDenyTilesNumber(), 4);

        assertThrows(RuntimeException.class, characterCard::incrementDenyTiles);

        characterCard.decrementDenyTiles();
        assertEquals(characterCard.getDenyTilesNumber(), 3);

        for(int i = 0; i < 3; i++) characterCard.decrementDenyTiles();
        assertThrows(RuntimeException.class, characterCard::decrementDenyTiles);
    }
}