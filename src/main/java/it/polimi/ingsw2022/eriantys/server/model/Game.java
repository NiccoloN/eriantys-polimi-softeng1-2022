package it.polimi.ingsw2022.eriantys.server.model;

import it.polimi.ingsw2022.eriantys.client.view.cli.AnsiCodes;
import it.polimi.ingsw2022.eriantys.messages.requests.ColoredPawnOriginDestination;
import it.polimi.ingsw2022.eriantys.server.model.board.Board;
import it.polimi.ingsw2022.eriantys.server.model.board.SchoolDashboard;
import it.polimi.ingsw2022.eriantys.server.model.cards.CardFactory;
import it.polimi.ingsw2022.eriantys.server.model.cards.CharacterCard;
import it.polimi.ingsw2022.eriantys.server.model.influence.InfluenceCalculator;
import it.polimi.ingsw2022.eriantys.server.model.pawns.ColoredPawn;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;
import it.polimi.ingsw2022.eriantys.server.model.pawns.StudentsBag;
import it.polimi.ingsw2022.eriantys.server.model.players.Player;
import it.polimi.ingsw2022.eriantys.server.model.players.Team;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

/**
 * This class represents the game instance, and contains all the game data.
 * @author Francesco Melegati
 * @author Emanuele Musto
 * @author Niccolò Nicolosi
 */
public class Game implements Serializable {
    
    // Predefined game values
    public static final int NUMBER_OF_STUDENTS_PER_COLOR = 26;
    public static final int NUMBER_OF_PROFESSORS_PER_COLOR = 1;
    public static final int MAX_NUMBER_OF_PLAYERS = 4;
    public static final int MIN_NUMBER_OF_PLAYERS = 2;
    
    private final Board board;
    private final List<ColoredPawn> students;
    private final StudentsBag studentsBag;
    private final List<ColoredPawn> professors;
    private final List<CharacterCard> characters;
    private final List<Player> playersStartOrder;
    private final List<Player> players;
    private final Map<String, Team> teams;
    private final Map<ColoredPawnOriginDestination, PawnColor> exchangesCausedByCharacters;
    private InfluenceCalculator influenceCalculator;
    private Player currentPlayer;
    private int characterIsland;
    private boolean gameEnding;
    
    /**
     * Constructs a new game
     * @param playerUsernames the usernames of the player in the game
     * @throws IOException if an IOException occurs while reading from files
     */
    public Game(String[] playerUsernames) throws IOException {
        
        teams = new HashMap<>(3);
        teams.put("WHITE", new Team(AnsiCodes.WHITE, "WHITE"));
        teams.put("BLACK", new Team(AnsiCodes.BLACK_BRIGHT, "BLACK"));
        teams.put("CYAN", new Team(AnsiCodes.CYAN, "CYAN"));
        
        players = generatePlayers(playerUsernames);
        playersStartOrder = new ArrayList<>(players);
        
        board = new Board(players);
        students = generatePawns(NUMBER_OF_STUDENTS_PER_COLOR);
        professors = generatePawns(NUMBER_OF_PROFESSORS_PER_COLOR);
        studentsBag = new StudentsBag();
        characters = new ArrayList<>(3);
        
        exchangesCausedByCharacters = new HashMap<>(3);
        gameEnding = false;
        
        placeFirstStudents();
        fillStudentsBag();
        fillSchools();
        chooseCharacters();
        assignHelpers();
    }
    
    public Team getTeam(String teamName) {
        
        Team team = teams.get(teamName);
        if (team == null) throw new NoSuchElementException();
        return team;
    }
    
    /**
     * Based on the index of the helper cards chosen by the players, this method decides on which order the players will play.
     */
    public void sortPlayersBasedOnHelperCard() {
        
        players.sort(Player::comparePriorityTo);
    }
    
    /**
     * This method is called whenever a student is moved to the school dashboard.
     * For every color, the player with the most students on the school dashboard gets the professor of that color.
     * Therefore, this method is used to check whether the movement of a student causes a movement of the professor of
     * the same color, and if true, moves the professor from one place to another.
     * @param color           the color of the student moved.
     * @param greaterAndEqual if true it's enough to reach the same number of student of the player with the professor
     *                        in order to get the professor (effect caused by a character), if false the number of student
     *                        need to be greater than the number of student of the player with the professor.
     */
    public void checkAndUpdateProfessor(PawnColor color, boolean greaterAndEqual) {
        
        SchoolDashboard winnerSchool = null;
        
        for (Player player : getPlayers()) {
            
            SchoolDashboard school = player.getSchool();
            if (school.containsProfessor(color)) {
                
                winnerSchool = school;
                break;
            }
        }
        
        if (winnerSchool == null) {
            
            for (Player player : players) {
                
                SchoolDashboard school = player.getSchool();
                if (school.countTableStudents(color) > 0) {
                    
                    school.addProfessor(getProfessor(color));
                    winnerSchool = player.getSchool();
                }
            }
            if (winnerSchool == null) return;
        }
        
        if (!greaterAndEqual) {
            
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
            if (!currentPlayerSchool.equals(winnerSchool) && currentPlayerSchool.countTableStudents(color) >= winnerSchool.countTableStudents(color)) {
                
                ColoredPawn professor = winnerSchool.removeProfessor(color);
                currentPlayerSchool.addProfessor(professor);
            }
        }
    }
    
    public ArrayList<Player> getPlayers() {
        
        return new ArrayList<>(players);
    }
    
    public ColoredPawn getProfessor(PawnColor color) {
        
        ColoredPawn professor = professors.stream().filter(x -> x.color == color).findFirst().orElseThrow();
        professors.remove(professor);
        return professor;
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
    
    public Player getPlayer(String username) {
        
        return players.stream().filter(player -> Objects.equals(player.getUsername(), username)).findFirst().orElseThrow();
    }
    
    public List<Player> getPlayersStartOrder() {
        
        return new ArrayList<>(playersStartOrder);
    }
    
    public Player getCurrentPlayer() {
        
        return currentPlayer;
    }
    
    public void setCurrentPlayer(Player currentPlayer) {
        
        this.currentPlayer = currentPlayer;
    }
    
    public InfluenceCalculator getInfluenceCalculator() {
        
        return influenceCalculator;
    }
    
    public void setInfluenceCalculator(InfluenceCalculator influenceCalculator) {
        
        this.influenceCalculator = influenceCalculator;
    }
    
    /**
     * Resets the usages of characters of every player. It's called at the beginning of every round, and it's used to
     * let one player use a character maximum once every round.
     */
    public void resetCharacterUses() {
        
        for (Player player : players) player.setCharacterUsed(false);
    }
    
    public void resetExchanges() {
        
        exchangesCausedByCharacters.clear();
    }
    
    public void setExchanges(ColoredPawnOriginDestination origin, PawnColor color) {
        
        exchangesCausedByCharacters.put(origin, color);
    }
    
    public PawnColor getExchange(ColoredPawnOriginDestination origin) {
        
        return exchangesCausedByCharacters.get(origin);
    }
    
    public int getCharacterIsland() {
        
        return characterIsland;
    }
    
    public void setCharacterIsland(int characterIsland) {
        
        this.characterIsland = characterIsland;
    }
    
    public boolean isGameEnding() {
        
        return gameEnding;
    }
    
    public void setGameEnding() {
        
        this.gameEnding = true;
    }
    
    /**
     * Checks which is the winner team when the game ends.
     * The winner team is the team with the higher number of placed towers in the islands.
     * @return the winner team.
     */
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
                    
                    if (player.getSchool().countProfessors() > winnerTeam.getLeader().getSchool().countProfessors())
                        winnerTeam = player.team;
                }
                else winnerTeam = player.team;
                
                winnerTeamTowers = playerTowers;
            }
        }
        return winnerTeam;
    }
    
    /**
     * Generates a list of players containing the given number of players, dividing them in teams
     * @param playerUsernames the usernames of the players to be generated
     * @return the generated list
     */
    private List<Player> generatePlayers(String[] playerUsernames) {
        
        final String errorMsg = String.format("Invalid number of players: must be >= %s and <= %s", MIN_NUMBER_OF_PLAYERS, MAX_NUMBER_OF_PLAYERS);
        
        final int numberOfPlayers = playerUsernames.length;
        
        if (numberOfPlayers > MAX_NUMBER_OF_PLAYERS || numberOfPlayers < MIN_NUMBER_OF_PLAYERS) {
            
            throw new RuntimeException(errorMsg);
        }
        
        List<Player> players = new ArrayList<>();
        Team team;
        for (int playerNumber = 0; playerNumber < numberOfPlayers; playerNumber++) {
            
            if (numberOfPlayers == 2 || numberOfPlayers == 4) {
                
                team = playerNumber % 2 == 0 ? getTeam("WHITE") : getTeam("BLACK");
            }
            else {
                
                switch (playerNumber) {
                    
                    case 0:
                        team = getTeam("WHITE");
                        break;
                    case 1:
                        team = getTeam("BLACK");
                        break;
                    case 2:
                        team = getTeam("CYAN");
                        break;
                    default:
                        throw new RuntimeException(errorMsg);
                }
            }
            players.add(new Player(playerUsernames[playerNumber], team));
        }
        return players;
    }
    
    /**
     * Generates a list of pawns containing the given number of pawns per color
     * @param numberPerColor the number of pawns per color to be generated
     * @return the generated list of pawns
     */
    private List<ColoredPawn> generatePawns(int numberPerColor) {
        
        if (numberPerColor <= 0) throw new RuntimeException("Invalid number of pawns");
        
        List<ColoredPawn> studentsList = new ArrayList<>();
        for (PawnColor color : PawnColor.values()) {
            
            for (int numberOfPawns = 0; numberOfPawns < numberPerColor; numberOfPawns++) {
                
                studentsList.add(new ColoredPawn(color));
            }
        }
        return studentsList;
    }
    
    /**
     * When the game start, this method is called to place a student for every island
     * except the one with mother nature and the one opposite to it.
     */
    private void placeFirstStudents() {
        
        int colorCounter;
        for (PawnColor color : PawnColor.values()) {
            
            colorCounter = 0;
            Iterator<ColoredPawn> iterator = students.iterator();
            while (iterator.hasNext()) {
                
                ColoredPawn student = iterator.next();
                if (student.color == color) {
                    
                    iterator.remove();
                    studentsBag.addStudent(student);
                    colorCounter++;
                    if (colorCounter >= 2) break;
                }
            }
        }
        for (int n = 1; n < board.getNumberOfIslands(); n++)
            if (n != board.getNumberOfIslands() / 2) board.getIsland(n).addStudent(studentsBag.extractRandomStudent());
    }
    
    /**
     * Adds every student pawn to the student bag.
     * @see StudentsBag
     */
    private void fillStudentsBag() {
        
        for (ColoredPawn student : students) studentsBag.addStudent(student);
        students.clear();
    }
    
    /**
     * Adds seven student pawns to the entrance of every player's school.
     */
    private void fillSchools() {
        
        int studentsPerSchool = players.size() == 3 ? 9 : 7;
        
        for (Player player : players) {
            
            SchoolDashboard school = player.getSchool();
            for (int n = 0; n < studentsPerSchool; n++) school.addToEntrance(studentsBag.extractRandomStudent());
        }
    }
    
    /**
     * Chooses randomly three of the possible twelve characters of the game.
     * @throws IOException when the path to the json file containing information regarding characters is wrong
     *                     or can't be accessed.
     */
    private void chooseCharacters() throws IOException {
        
        List<CharacterCard> characterCards = new ArrayList<>(12);
        for (int n = 1; n <= 12; n++) characterCards.add(CardFactory.createCharacterCard(n));
        for (int n = 0; n < 3; n++)
            characters.add(characterCards.remove((int) (Math.random() * characterCards.size())));
    }
    
    /**
     * Gives to every player ten helper cards to use during the game.
     * @throws IOException when the path to the json file containing information regarding helpers is wrong
     *                     or can't be accessed.
     */
    private void assignHelpers() throws IOException {
        
        for (Player player : players)
            for (int n = 1; n <= 10; n++)
                player.addHelperCard(CardFactory.createHelperCard(n));
    }
}
