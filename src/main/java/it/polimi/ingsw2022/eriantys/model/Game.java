package it.polimi.ingsw2022.eriantys.model;
import it.polimi.ingsw2022.eriantys.controller.GameMode;
import it.polimi.ingsw2022.eriantys.model.board.Board;
import it.polimi.ingsw2022.eriantys.model.cards.CharacterCard;
import it.polimi.ingsw2022.eriantys.model.pawns.ColoredPawn;
import it.polimi.ingsw2022.eriantys.model.pawns.PawnColor;
import it.polimi.ingsw2022.eriantys.model.pawns.StudentsBag;
import it.polimi.ingsw2022.eriantys.model.players.Mage;
import it.polimi.ingsw2022.eriantys.model.players.Player;
import it.polimi.ingsw2022.eriantys.model.players.Team;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the game instance.
 * @author Francesco Melegati
 */
public class Game {
    // Predefined game values
    final int NUMBER_OF_STUDENTS_PER_COLOR = 26;
    final int NUMBER_OF_PROFESSORS_PER_COLOR = 1;
    final int MAX_NUMBER_OF_PLAYERS = 4;

    private GameMode gameMode;
    private final Board board;
    private final List<ColoredPawn> students;
    private final StudentsBag studentsBag;
    private ColoredPawn studentToMove;
    private final List<ColoredPawn> professors;
    private final List<CharacterCard> characters;
    private InfluenceCalculator influenceCalculator;
    private final List<Player> players;
    private Player currentPlayer;
    private boolean gameEnding;

    public Game(int numberOfPlayers, GameMode mode) {
        gameEnding = false;
        students = generatePawns(NUMBER_OF_STUDENTS_PER_COLOR);
        players = generatePlayers(numberOfPlayers);
        professors = generatePawns(NUMBER_OF_PROFESSORS_PER_COLOR);
        studentsBag = new StudentsBag();
        board = new Board(players);
        characters = new ArrayList<>();
    }

    /**
     * Given a number of students to be created for each color it creates a List of pawns with the same amount of pawn
     * for each color.
     * @param numberForColor number of pawns per color to be generated
     * @return list of students
     */
    private List<ColoredPawn> generatePawns(int numberForColor) {
        List<ColoredPawn> studentsList = new ArrayList<>();
        for (PawnColor color : PawnColor.values()) {
            for (int numberOfPawns = 0; numberOfPawns < numberForColor; numberOfPawns++) {
                studentsList.add(new ColoredPawn(color));
            }
        }
        return  studentsList;
    }

    /**
     * Given number of players it creates a List of players divided in teams based on number of players
     * @param numberOfPlayers number of players to be generated
     * @return list of players divided by team based on number of players
     */
    private List<Player> generatePlayers(int numberOfPlayers) {
        if (numberOfPlayers > MAX_NUMBER_OF_PLAYERS || numberOfPlayers < 0) {
            throw new RuntimeException("Wrong players number");
        }
        List<Player> players = new ArrayList<>();
        Mage[] mageNames = Mage.values();
        Team team;
        for (int playerNumber = 0; playerNumber < numberOfPlayers; playerNumber++) {
            if (numberOfPlayers == 2 || numberOfPlayers == 4) {
                team = playerNumber % 2 == 0 ? Team.WHITE : Team.BLACK;
            } else {
                switch (playerNumber) {
                    case 0:
                        team = Team.WHITE;
                        break;
                    case 1:
                        team = Team.BLACK;
                        break;
                    case 2:
                        team = Team.GRAY;
                        break;
                    default:
                        throw new RuntimeException("Wrong players number");
                }
            }
            players.add(new Player(team, mageNames[playerNumber]));

        }
        return players;
    }

    public void startGame() {
    }

    public void calculatePoints() {
        // TODO: implement it
    }

    public Board getBoard() {
        return board;
    }

    public StudentsBag getStudentsBag() {
        return studentsBag;
    }

    public CharacterCard getCharacterCard(int index) {
        return characters.get(index);
    }

    public ColoredPawn getProfessor(int index) {
        return professors.get(index);
    }

    public Player getPlayer(int index) {
        return players.get(index);
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public ColoredPawn getStudentToMove() {
        return studentToMove;
    }

    public void setStudentToMove(ColoredPawn studentToMove) {
        this.studentToMove = studentToMove;
    }

    public InfluenceCalculator getInfluenceCalculator() {
        return influenceCalculator;
    }

    public void setInfluenceCalculator(InfluenceCalculator influenceCalculator) {
        this.influenceCalculator = influenceCalculator;
    }

    public boolean isGameEnding() {
        return  gameEnding;
    }

    public void setGameEnding() {
        this.gameEnding = true;
    }
}
