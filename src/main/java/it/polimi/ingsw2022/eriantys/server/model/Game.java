package it.polimi.ingsw2022.eriantys.server.model;
import it.polimi.ingsw2022.eriantys.server.controller.BasicGameMode;
import it.polimi.ingsw2022.eriantys.server.controller.ExpertGameMode;
import it.polimi.ingsw2022.eriantys.server.controller.GameMode;
import it.polimi.ingsw2022.eriantys.server.controller.Mode;
import it.polimi.ingsw2022.eriantys.server.model.board.Board;
import it.polimi.ingsw2022.eriantys.server.model.cards.CardFactory;
import it.polimi.ingsw2022.eriantys.server.model.cards.CharacterCard;
import it.polimi.ingsw2022.eriantys.server.model.influence.InfluenceCalculator;
import it.polimi.ingsw2022.eriantys.server.model.pawns.ColoredPawn;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;
import it.polimi.ingsw2022.eriantys.server.model.pawns.StudentsBag;
import it.polimi.ingsw2022.eriantys.server.model.players.Mage;
import it.polimi.ingsw2022.eriantys.server.model.players.Player;
import it.polimi.ingsw2022.eriantys.server.model.players.Team;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class represents the game instance.
 * @author Francesco Melegati
 */
public class Game {
    // Predefined game values
    public static final int NUMBER_OF_STUDENTS_PER_COLOR = 26;
    public static final int NUMBER_OF_PROFESSORS_PER_COLOR = 1;
    public static final int MAX_NUMBER_OF_PLAYERS = 4;
    public static final int MIN_NUMBER_OF_PLAYERS = 2;

    private final GameMode gameMode;
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

    public Game(String[] playerUsernames, Mode mode) throws IOException {

        gameEnding = false;
        players = generatePlayers(playerUsernames);
        board = new Board(players);
        students = generatePawns(NUMBER_OF_STUDENTS_PER_COLOR);
        professors = generatePawns(NUMBER_OF_PROFESSORS_PER_COLOR);
        studentsBag = new StudentsBag();
        characters = new ArrayList<>(3);
        gameMode = mode == Mode.BASIC ? new BasicGameMode(this) : new ExpertGameMode(this);

        placeFirstStudents();
        fillStudentsBag();
        chooseCharacters();
        assignHelpers();
    }

    private void placeFirstStudents() {

        int colorCounter;
        for(PawnColor color : PawnColor.values()) {

            colorCounter = 0;
            Iterator<ColoredPawn> iterator = students.iterator();
            while(iterator.hasNext()) {

                ColoredPawn student = iterator.next();
                if(student.color == color) {

                    iterator.remove();
                    studentsBag.addStudent(student);
                    colorCounter++;
                    if(colorCounter >= 2) break;
                }
            }
        }
        for(int n = 1; n < board.getNumberOfIslands(); n++)
            if(n != board.getNumberOfIslands() / 2)
                board.getIsland(n).addStudent(studentsBag.extractRandomStudent());
    }

    private void fillStudentsBag() {

        for(ColoredPawn student : students) studentsBag.addStudent(student);
        students.clear();
    }

    private void chooseCharacters() throws IOException {

        List<CharacterCard> characterCards = new ArrayList<>(12);
        for(int n = 1; n <= 12; n++) characterCards.add(CardFactory.createCharacterCard(n));
        for(int n = 0; n < 3; n++) characters.add(characterCards.remove((int) (Math.random() * characterCards.size())));
    }

    private void assignHelpers() throws IOException {

        for(Player player : players)
            for(int  n = 1; n <= 10; n++)
                player.addHelperCard(CardFactory.createHelperCard(n, player.mage));
    }

    /**
     * Generates a list of pawns containing the given number of pawns per color
     * @param numberPerColor the number of pawns per color to be generated
     * @return the generated list of pawns
     */
    private List<ColoredPawn> generatePawns(int numberPerColor) {
        if (numberPerColor <= 0) {
            throw new RuntimeException("Invalid number of pawns");
        }
        List<ColoredPawn> studentsList = new ArrayList<>();
        for (PawnColor color : PawnColor.values()) {
            for (int numberOfPawns = 0; numberOfPawns < numberPerColor; numberOfPawns++) {
                studentsList.add(new ColoredPawn(color));
            }
        }
        return  studentsList;
    }

    /**
     * Generates a list of players containing the given number of players, dividing them in teams
     * @param playerUsernames the usernames of the players to be generated
     * @return the generated list
     */
    private List<Player> generatePlayers(String[] playerUsernames) {
        final String errorMsg = String.format("Invalid number of players: must be >= %s and <= %s",
                MIN_NUMBER_OF_PLAYERS, MAX_NUMBER_OF_PLAYERS);

        final int numberOfPlayers = playerUsernames.length;

        if (numberOfPlayers > MAX_NUMBER_OF_PLAYERS || numberOfPlayers < MIN_NUMBER_OF_PLAYERS) {
            throw new RuntimeException(errorMsg);
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
                        throw new RuntimeException(errorMsg);
                }
            }
            players.add(new Player(playerUsernames[playerNumber], team, mageNames[playerNumber]));
        }
        return players;
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

    public int getNumberOfCharacters() {
        return characters.size();
    }

    public CharacterCard getCharacter(int index) {
        return characters.get(index);
    }

    public ColoredPawn getProfessor(int index) {
        return professors.get(index);
    }

    public Player getPlayer(int index) {
        return players.get(index);
    }

    public Player[] getPlayers() {
        return players.toArray(new Player[0]);
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
