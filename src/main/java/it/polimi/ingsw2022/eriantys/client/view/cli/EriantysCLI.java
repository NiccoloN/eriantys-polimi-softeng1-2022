package it.polimi.ingsw2022.eriantys.client.view.cli;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.client.view.View;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.CLIScene;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.states.CharacterSelection;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.states.CloudSelection;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.states.HelperSelection;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.states.IslandSelection;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.menuScene.MenuScene;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.GameScene;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.menuScene.states.EnterUsername;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.menuScene.states.LobbyWaiting;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.menuScene.states.NumberOfPlayersSelection;
import it.polimi.ingsw2022.eriantys.messages.Message;

import it.polimi.ingsw2022.eriantys.messages.Move.MoveType;
import it.polimi.ingsw2022.eriantys.messages.toClient.MoveRequestMessage;
import it.polimi.ingsw2022.eriantys.messages.toClient.changes.GameInitChange;

import it.polimi.ingsw2022.eriantys.messages.toClient.changes.CharacterCardsChange;

import it.polimi.ingsw2022.eriantys.messages.toClient.changes.IslandChange;
import it.polimi.ingsw2022.eriantys.messages.toClient.changes.*;
import it.polimi.ingsw2022.eriantys.messages.toServer.GameSettings;
import it.polimi.ingsw2022.eriantys.messages.toServer.PerformedMoveMessage;
import it.polimi.ingsw2022.eriantys.server.controller.Mode;
import it.polimi.ingsw2022.eriantys.server.model.players.Team;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeoutException;

import static it.polimi.ingsw2022.eriantys.client.view.cli.AnsiCodes.*;

/**
 * This class represents the command line interface (CLI) of the game
 * @author Niccolò Nicolosi
 */
public class EriantysCLI implements View {

    private static final float FRAME_TIME = 1 / 60f;
    private static final int FRAME_WIDTH = 189, FRAME_HEIGHT = 58;

    private static final String TERMINAL_RESET = "\u001Bc\u001B[3J\u001Bc\u001B[H";
    private static final String TERMINAL_RESIZE = "\u001B[8;" + FRAME_HEIGHT + ";" + FRAME_WIDTH + "t";
    private static final String TERMINAL_HOME = "\u001B[H" + RESET;

    private final Terminal terminal;
    private int prevWidth, prevHeight;

    private boolean running;

    private final char[] inputChars;
    private boolean inputProcessed;

    private final Frame frame;
    private boolean showLog;

    private GameScene gameScene;
    private CLIScene currentScene;

    /**
     * Constructs the CLI and all of its components
     * @throws IOException if the terminal instance could not be built
     */
    public EriantysCLI() throws IOException {

        //build terminal instance
        terminal = TerminalBuilder
                .builder()
                .system(true)
                .jna(true)
                .build();
        terminal.writer().print(TERMINAL_RESET + TERMINAL_RESIZE);
        terminal.flush();

        running = false;

        //build input buffer
        inputChars = new char[3];
        inputProcessed = false;

        //initialize frame
        frame = new Frame(FRAME_WIDTH, FRAME_HEIGHT);
        showLog = false;

        currentScene = new MenuScene(this, frame.getWidth(), frame.getHeight());
    }

    /**
     * Starts the CLI. Once the CLI is started, the current state of the game is visualized in the terminal window and
     * inputs are passed to the controller to evolve the game state
     * @throws TimeoutException if the terminal window could not be correctly resized
     */
    @Override
    public void start(boolean showLog) throws TimeoutException {

        this.showLog = showLog;
        running = true;

        //the input thread will take care of accepting new inputs at the right time and passing them to the controller
        Thread inputThread = new Thread(() -> {

            synchronized(this) { terminal.enterRawMode(); }

            while(true) {

                try {

                    synchronized(this) {

                        if (inputProcessed) wait();
                        notify();
                    }

                    //waits for new input
                    Arrays.fill(inputChars, '\0');
                    inputChars[0] = (char) terminal.reader().read();

                    //if the fist character read is an escape character, waits for an ansi escape sequence
                    if (inputChars[0] == ESCAPE_CHAR) {

                        int c;
                        for (int n = 1; n < inputChars.length; n++) {

                            c = terminal.reader().read(1);
                            if (c < 0) break;
                            inputChars[n] = (char) c;
                        }
                    }
                    Input input = new Input(inputChars[0], inputChars[1], inputChars[2]);

                    synchronized(this) {

                        //manages the input based on the current state
                        currentScene.manageInput(input);
                        inputProcessed = true;
                        notify();
                    }
                }
                catch(IOException | InterruptedException e) {

                    printException(e);
                }
            }
        });
        inputThread.start();

        //update loop
        long lastFrameTime, delta;
        while(running) {

            lastFrameTime = System.nanoTime();
            delta = 0;
            while(delta < FRAME_TIME * 1000000000) {

                try {

                    Thread.sleep(0, 100);
                }
                catch(InterruptedException e) {

                    printException(e);
                }
                delta = System.nanoTime() - lastFrameTime;
            }
            synchronized(this) {

                if (running) update();
                inputProcessed = false;
                notify();
            }
        }
    }

    /**
     * Updates the terminal window with the current state of the game
     * @throws TimeoutException if the terminal window could not be correctly resized
     */
    private void update() throws TimeoutException {

        //resize terminal window to the correct size
        long start = System.nanoTime();
        long time;
        long timeout = 5000000000L;
        while (!acceptableWindowSize()) {

            terminal.writer().print(TERMINAL_RESIZE);

            try {

                Thread.sleep(1);
            }
            catch (InterruptedException e) {

                e.printStackTrace();
            }

            time = System.nanoTime() - start;
            if (time >= timeout)
                throw new TimeoutException("Could not correctly resize terminal window (elapsed time: " + time / 1000000 + "ms)\n"
                                           + "This application needs a terminal window of at least " + FRAME_WIDTH + " columns and " + FRAME_HEIGHT + " rows to run\n"
                                           + "Try to manually resize your terminal window or to reduce the font size of your terminal");
        }

        //reset terminal if size changed or buffer is too big
        if (terminal.getWidth() != prevWidth || terminal.getHeight() != prevHeight) terminal.writer().print(TERMINAL_RESET);
        prevWidth = terminal.getWidth();
        prevHeight = terminal.getHeight();

        frame.clear();

        currentScene.printToFrame(frame);

        //build the frame string
        StringBuilder frameBuilder = new StringBuilder();
        frameBuilder.append(TERMINAL_HOME);
        frameBuilder.append(frame.getAnsiString(Math.max(terminal.getWidth(), FRAME_WIDTH), Math.max(terminal.getHeight(), FRAME_HEIGHT)));

        //append log if enabled
        if(showLog) frameBuilder.append(TERMINAL_HOME).append(EriantysClient.getInstance().getLog(frame.getHeight()));

        //print the frame string to terminal
        terminal.writer().print(frameBuilder);
        terminal.flush();
    }

    /**
     * Sets the scene to show
     * @param scene the scene to show
     */
    public void setScene(CLIScene scene) {

        this.currentScene = scene;
        if(scene instanceof GameScene) gameScene = (GameScene) scene;
    }

    /**
     * Forces clear and reset of the terminal window. Slow method: it is strongly recommended not to use it in loops
     */
    private void clear() {

        terminal.writer().print(TERMINAL_RESET);
        terminal.flush();
    }

    /**
     * @return whether the terminal window is of an acceptable size
     */
    private boolean acceptableWindowSize() {

        return terminal.getWidth() >= FRAME_WIDTH && terminal.getHeight() >= FRAME_HEIGHT;
    }

    /**
     * Stops cli updating, clears the terminal window and prints the given exception
     * @param e the exception to print
     */
    private void printException(Exception e) {

        running = false;
        clear();
        e.printStackTrace();
    }

    /**
     * Sets whether the log should be visualized or not
     * @param log true to visualize the log, false otherwise
     */
    public void showLog(boolean log) {

        this.showLog = log;
    }

    @Override
    public void askUsername(Message requestMessage) {

        if(!(currentScene instanceof MenuScene)) throw new RuntimeException("The current scene must be a MenuScene to ask for a username");
        currentScene.setState(new EnterUsername(this, (MenuScene) currentScene, requestMessage));
    }

    @Override
    public void askGameSettings(Message requestMessage) {

        if(!(currentScene instanceof MenuScene)) throw new RuntimeException("The current scene must be a MenuScene to ask for game settings");
        currentScene.setState(new NumberOfPlayersSelection(this, (MenuScene) currentScene, requestMessage));
    }

    @Override
    public void showUpdatedLobby(String[] playerUsernames, GameSettings gameSettings) {

        if(!(currentScene instanceof MenuScene)) throw new RuntimeException("The current scene must be a MenuScene to show the game lobby");
        currentScene.setState(new LobbyWaiting(this, (MenuScene) currentScene, playerUsernames, gameSettings));
    }

    @Override
    public void startGame(Player[] players, Mode gameMode) {

        setScene(new GameScene(this, currentScene.getWidth(), currentScene.getHeight(), players, gameMode));
    }

    @Override
    public void applyChange(CharacterCardsChange change) {

        if(gameScene != null) gameScene.applyChange(change);
        else throw new RuntimeException("GameScene must be initialized in order to apply an update");
    }

    @Override
    public void applyChange(HelperCardsChange change) {

        if(gameScene != null) gameScene.applyChange(change);
        else throw new RuntimeException("GameScene must be initialized in order to apply an update");
    }

    @Override
    public void applyChange(IslandChange change) {

        if(gameScene != null) gameScene.applyChange(change);
        else throw new RuntimeException("GameScene must be initialized in order to apply an update");
    }

    @Override
    public void applyChange(CloudChange change) {

        if(gameScene != null) gameScene.applyChange(change);
        else throw new RuntimeException("GameScene must be initialized in order to apply an update");
    }

    @Override
    public void applyChange(SchoolChange change) {

        if(gameScene != null) gameScene.applyChange(change);
        else throw new RuntimeException("GameScene must be initialized in order to apply an update");
    }

    @Override
    public void applyChange(PlayerChange change) {

        if(gameScene != null) gameScene.applyChange(change);
        else throw new RuntimeException("GameScene must be initialized in order to apply an update");
    }

    @Override
    public void applyChange(StudentsBagChange change) {


    }

    @Override
    public void askMoveType(MoveRequestMessage requestMessage){
        if(!(currentScene instanceof GameScene))
            throw new RuntimeException("The current scene must be a MenuScene to ask for a username");
        switch(requestMessage.requestedMove) {
            case CHOOSE_HELPER_CARD:
                currentScene.setState(new HelperSelection(this, gameScene, requestMessage));
            case CHOOSE_CLOUD:
                currentScene.setState(new CloudSelection(this, gameScene, requestMessage));
            case MOVE_MOTHER_NATURE:
                currentScene.setState(new IslandSelection(this, gameScene, requestMessage));
            case CHOOSE_CHARACTER_CARD:
                //do nothing
            case MOVE_STUDENT:
                //TODO
        }
    }
}
