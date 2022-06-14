package it.polimi.ingsw2022.eriantys.server.model;
import it.polimi.ingsw2022.eriantys.messages.requests.CharactersColorOrigin;
import it.polimi.ingsw2022.eriantys.server.model.board.Board;
import it.polimi.ingsw2022.eriantys.server.model.board.CompoundIslandTile;
import it.polimi.ingsw2022.eriantys.server.model.board.SchoolDashboard;
import it.polimi.ingsw2022.eriantys.server.model.cards.CardFactory;
import it.polimi.ingsw2022.eriantys.server.model.cards.CharacterCard;
import it.polimi.ingsw2022.eriantys.server.model.influence.InfluenceCalculator;
import it.polimi.ingsw2022.eriantys.server.model.pawns.ColoredPawn;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;
import it.polimi.ingsw2022.eriantys.server.model.pawns.StudentsBag;
import it.polimi.ingsw2022.eriantys.server.model.players.Mage;
import it.polimi.ingsw2022.eriantys.server.model.players.Player;
import it.polimi.ingsw2022.eriantys.server.model.players.Team;
import javafx.util.Pair;

import java.io.IOException;
import java.util.*;

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

    private final Board board;
    private final List<ColoredPawn> students;
    private final StudentsBag studentsBag;
    private ColoredPawn studentToMove;
    private final List<ColoredPawn> professors;
    private final List<CharacterCard> characters;
    private InfluenceCalculator influenceCalculator;
    private final List<Player> players;
    private Player currentPlayer;

    //TODO mettere un boolean nel player
    private final Map<String, Integer> characterUses = new HashMap<>(4);
    private final Map<CharactersColorOrigin, PawnColor> exchangesCausedByCharacters = new HashMap<>(3);
    private boolean abortMessageReceived = false;
    private boolean gameEnding;

    public Game(String[] playerUsernames) throws IOException {

        gameEnding = false;
        players = generatePlayers(playerUsernames);
        board = new Board(players);
        students = generatePawns(NUMBER_OF_STUDENTS_PER_COLOR);
        professors = generatePawns(NUMBER_OF_PROFESSORS_PER_COLOR);
        studentsBag = new StudentsBag();
        characters = new ArrayList<>(3);

        placeFirstStudents();
        fillStudentsBag();
        fillSchools();
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
            if(n != board.getNumberOfIslands() / 2 )
                board.getIsland(n).addStudent(studentsBag.extractRandomStudent());
    }

    private void fillStudentsBag() {

        for(ColoredPawn student : students) studentsBag.addStudent(student);
        students.clear();
    }

    private void fillSchools() {

        for (Player player : players) {
            
            SchoolDashboard school = player.getSchool();
            for (int n = 0; n < 7; n++) school.addToEntrance(studentsBag.extractRandomStudent());
        }
    }

    private void chooseCharacters() throws IOException {

        List<CharacterCard> characterCards = new ArrayList<>(12);
        for(int n = 1; n <= 12; n++) characterCards.add(CardFactory.createCharacterCard(n));
        for(int n = 0; n < 3; n++) characters.add(characterCards.remove( 0
                //(int) (Math.random() * characterCards.size())
                ));
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

    public void sortPlayersBasedOnHelperCard() {

        players.sort(Player::comparePriorityTo);
    }

    public void checkAndUpdateProfessor(PawnColor color, boolean characterCardUsage) {

        SchoolDashboard winnerSchool = null;

        for (Player player : getPlayers()) {
            SchoolDashboard schoolDashboard = player.getSchool();
            if (schoolDashboard.containsProfessor(color)) {
                winnerSchool = schoolDashboard;
                break;
            }
        }

        if (winnerSchool == null) {
            for (Player player : players) {
                if (player.getSchool().countTableStudents(color) > 0) {
                    player.getSchool().addProfessor(getProfessor(color));
                    winnerSchool = player.getSchool();
                }
            }
            if (winnerSchool == null) return;
        }

        if(!characterCardUsage){
            for (Player player : getPlayers()) {
                SchoolDashboard school = player.getSchool();
                if (school != winnerSchool && school.countTableStudents(color) > winnerSchool.countTableStudents(color)) {
                    ColoredPawn professor;
                    professor = winnerSchool.removeProfessor(color);

                    winnerSchool = school;
                    winnerSchool.addProfessor(professor);
                }
            }
        }
        else {
            SchoolDashboard currentPlayerSchool = currentPlayer.getSchool();
            if(!currentPlayerSchool.equals(winnerSchool) && currentPlayerSchool.countTableStudents(color) >= winnerSchool.countTableStudents(color)) {
                ColoredPawn professor = winnerSchool.removeProfessor(color);
                currentPlayerSchool.addProfessor(professor);
            }
        }

    }

    /**
     * Checks if the selected island has to be merged with their neighbor islands.
     * @param island the selected island
     * @return integer representing the status of the merge
     *      0: nothing has merged
     *      1: next island is merged
     *      -1: previous island is merged
     *      2: both islands are merged
     */
    public int checkAndMergeIslands(CompoundIslandTile island) {
        int wereSomeIslandsMerged = 0;
        int islandIndex = board.getIslandIndex(island);
        int nextIslandIndex = (islandIndex + 1) % board.getNumberOfIslands();
        int previousIslandIndex = (islandIndex - 1) % board.getNumberOfIslands();

        CompoundIslandTile nextIsland = board.getIsland(nextIslandIndex);
        CompoundIslandTile previousIsland = board.getIsland(previousIslandIndex);

        if (nextIsland.getTeam().equals(island.getTeam())) {
            board.mergeIslands(islandIndex, nextIslandIndex);
            wereSomeIslandsMerged = 1;
        }

        if (previousIsland.getTeam().equals(island.getTeam())) {
            board.mergeIslands(islandIndex, previousIslandIndex);
            wereSomeIslandsMerged = wereSomeIslandsMerged == 0 ? -1 : 2;
        }
        return wereSomeIslandsMerged;
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

    public CharacterCard getCharacterOfIndex(int index) {

        return characters.stream().filter((x) -> x.index == index).findAny().orElseThrow();
    }

    public ColoredPawn getProfessor(PawnColor color) {

        ColoredPawn professor = professors.stream().filter(x -> x.color == color).findFirst().orElseThrow();
        professors.remove(professor);
        return professor;
    }

    public Player getPlayer(String username) {

        return players.stream().filter(player -> Objects.equals(player.username, username)).findFirst().orElseThrow();
    }

    public ArrayList<Player> getPlayers() {
        return new ArrayList<>(players);
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

    public void setInfluenceCalculator(InfluenceCalculator influenceCalculator) { this.influenceCalculator = influenceCalculator; }

    public void resetCharacterUses(){ for(Player player : players) characterUses.put(player.username, 0); }

    public void incrementCharacterUses(String username){ characterUses.put(username, characterUses.get(username)+1); }

    public int getCharacterUses(String username){ return characterUses.get(username); }
    public void resetExchanges() { exchangesCausedByCharacters.clear(); }

    public void setExchanges(CharactersColorOrigin origin, PawnColor color) { exchangesCausedByCharacters.put(origin, color); }

    public PawnColor getExchange(CharactersColorOrigin origin) { return exchangesCausedByCharacters.get(origin); }

    public void setAbortMessageReceived(boolean value) { abortMessageReceived = value; }
    public boolean getAbortMessageReceived() { return abortMessageReceived; }

    public boolean isGameEnding() {
        return  gameEnding;
    }

    public void setGameEnding() {
        this.gameEnding = true;
    }

    public Team checkWinner() {
        Team winnerTeam = null;
        int winnerTeamTowers = -1;
        for (Player player : getPlayers()) {
            int playerTowers = player.getSchool().getTowers();

            if (winnerTeam == null && player.isTeamLeader) {
                winnerTeam = player.team;
                winnerTeamTowers = playerTowers;
            }

            if (player.isTeamLeader && playerTowers <= winnerTeamTowers) {
                if (playerTowers == winnerTeamTowers) {
                    if (player.getSchool().countProfessors() > winnerTeam.getLeader().getSchool().countProfessors()) {
                        winnerTeam = player.team;
                        winnerTeamTowers = playerTowers;
                    }
                } else {
                    winnerTeam = player.team;
                    winnerTeamTowers = playerTowers;
                }
            }
        }
        return winnerTeam;
    }
}
