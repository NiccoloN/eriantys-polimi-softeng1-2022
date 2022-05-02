package it.polimi.ingsw2022.eriantys.server.model.pawns;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StudentsBagTest {

    private StudentsBag studentsBag;

    @BeforeEach
    void setUp() {

        studentsBag = new StudentsBag();
    }

    @Test
    void isEmpty() {

        assertTrue(studentsBag.isEmpty());
        studentsBag.addStudent(new ColoredPawn(PawnColor.RED));
        assertFalse(studentsBag.isEmpty());
    }

    @Test
    void addStudent() {

        assertTrue(studentsBag.isEmpty());
        ColoredPawn student = new ColoredPawn(PawnColor.RED);
        studentsBag.addStudent(student);
        assertFalse(studentsBag.isEmpty());
        assertThrows(RuntimeException.class, () -> studentsBag.addStudent(student));
        assertThrows(AssertionError.class, () -> studentsBag.addStudent(null));
    }

    @Test
    void extractRandomStudent() {

        studentsBag.addStudent(new ColoredPawn(PawnColor.RED));
        studentsBag.addStudent(new ColoredPawn(PawnColor.YELLOW));
        studentsBag.addStudent(new ColoredPawn(PawnColor.BLUE));
        studentsBag.addStudent(new ColoredPawn(PawnColor.PINK));
        studentsBag.addStudent(new ColoredPawn(PawnColor.GREEN));
        studentsBag.extractRandomStudent();
        studentsBag.extractRandomStudent();
        studentsBag.extractRandomStudent();
        studentsBag.extractRandomStudent();
        studentsBag.extractRandomStudent();
        assertTrue(studentsBag.isEmpty());
        assertThrows(RuntimeException.class, studentsBag::extractRandomStudent);
    }
}