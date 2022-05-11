package it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.client.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.client.view.cli.Frame;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.CLIScene;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.components.AnimatedCLIComponent;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.components.BasicCLIComponent;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.components.CLIComponent;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.components.TextAreaCLIComponent;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.components.CharacterCardCLIComponent;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.components.CloudCLIComponent;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.components.HelperCardCLIComponent;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.components.IslandCLIComponent;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.components.player.PlayerStatusCLIComponent;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.states.ViewOnly;
import it.polimi.ingsw2022.eriantys.messages.toClient.changes.GameInitChange;
import it.polimi.ingsw2022.eriantys.messages.toClient.changes.IslandChange;
import it.polimi.ingsw2022.eriantys.server.controller.Mode;
import it.polimi.ingsw2022.eriantys.server.model.cards.CharacterCard;
import it.polimi.ingsw2022.eriantys.server.model.cards.HelperCard;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;
import it.polimi.ingsw2022.eriantys.server.model.players.Team;

import java.security.InvalidParameterException;
import java.util.*;

import static it.polimi.ingsw2022.eriantys.client.view.cli.AnsiCodes.*;

/**
 * This class represents a game scene
 * @author Niccolò Nicolosi
 */
public class GameScene extends CLIScene {
    
    private static final String TEAM_WHITE_COLOR = WHITE_BRIGHT;
    private static final String TEAM_BLACK_COLOR = BLACK_BRIGHT;
    private static final String TEAM_GRAY_COLOR = WHITE;
    
    private CLIComponent prompt;
    private final BasicCLIComponent title;
    private final BasicCLIComponent line;
    private final BasicCLIComponent skyBackground;
    private final AnimatedCLIComponent[] decorativeClouds;
    private final TextAreaCLIComponent hintTextArea;
    private final TextAreaCLIComponent effectTextArea;
    private final IslandCLIComponent[] islands;
    private final List<CloudCLIComponent> clouds;
    private final List<PlayerStatusCLIComponent> players;
    private List<HelperCardCLIComponent> helpers;
    private CharacterCardCLIComponent[] characters;

    /**
     * Constructs a game scene
     * @param cli the cli to associate to this scene
     * @param width the width of this scene
     * @param height the height of this scene
     * @param playerUsernames the usernames of each player
     * @param playerTeams the team of each player (must be of the same length of playerUsernames)
     * @param gameMode the mode of the game
     */
    public GameScene(EriantysCLI cli, int width, int height, String[] playerUsernames, Team[] playerTeams, Mode gameMode) {

        super(cli, width, height);

        if(playerTeams.length != playerUsernames.length)
            throw new InvalidParameterException("Invalid playerTeams for the given playerUsernames: they must be of the same length");

        //build title component
        title = new BasicCLIComponent(73, ("\0________\0\0\0\0\0\0\0\0\0\0\0\0__\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0__\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\n" + "|        \\\0\0\0\0\0\0\0\0\0\0|  \\\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0|  \\\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\n" + "| $$$$$$$$\0\0______\0\0\0\\$$\0\0______\0\0\0_______\0\0_| $$_\0\0\0\0__\0\0\0\0__\0\0\0_______\0\n" + "| $$__\0\0\0\0\0/      \\\0|  \\\0|      \\\0|       \\|   $$ \\\0\0|  \\\0\0|  \\\0/       \\\n" + "| $$  \\\0\0\0|  $$$$$$\\| $$\0\0\\$$$$$$\\| $$$$$$$\\\\$$$$$$\0\0| $$\0\0| $$|  $$$$$$$\n" + "| $$$$$\0\0\0| $$\0\0\0\\$$| $$\0/      $$| $$\0\0| $$\0| $$\0__\0| $$\0\0| $$\0\\$$    \\\0\n" + "| $$_____\0| $$\0\0\0\0\0\0| $$|  $$$$$$$| $$\0\0| $$\0| $$|  \\| $$__/ $$\0_\\$$$$$$\\\n" + "| $$     \\| $$\0\0\0\0\0\0| $$\0\\$$\0\0\0\0$$| $$\0\0| $$\0\0\\$$  $$\0\\$$    $$|       $$\n" + "\0\\$$$$$$$$\0\\$$\0\0\0\0\0\0\0\\$$\0\0\\$$$$$$$\0\\$$\0\0\0\\$$\0\0\0\\$$$$\0\0_\\$$$$$$$\0\\$$$$$$$\0\n" + "\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0|  \\__| $$\0\0\0\0\0\0\0\0\0\0\n" + "\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\\$$    $$\0\0\0\0\0\0\0\0\0\0\n" + "\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\\$$$$$$\0\0\0\0\0\0\0\0\0\0\0\n").split("\n"));
        title.setColor(YELLOW + BLUE_BACKGROUND_BRIGHT);

        //build sky background component
        String[] backgroundRows = new String[title.getHeight() + 1];
        Arrays.fill(backgroundRows, " ".repeat(width));
        skyBackground = new BasicCLIComponent(width, backgroundRows);
        skyBackground.setColor(BLUE_BACKGROUND_BRIGHT);

        //build sky separator line
        line = new BasicCLIComponent(width, new String[] {"_".repeat(width) });
        line.setColor(YELLOW + BLUE_BACKGROUND_BRIGHT);

        //build decorative cloud components
        String cloud1 = "\0\0\0\0._\0\0\0\0\0\n" + "\0.-(`  )\0\0\0\n" + ":(      ))\0\n" + "`(    )  ))\n" + "\0\0` __.:'\0\0\n";
        String cloud2 = "\0\0\0\0\0_\0\0\0\0\0\0\n" + "\0.:(`  )`.\0\0\n" + ":(   .    )\0\n" + "`.  (    ) )\n" + "\0\0` _`  ) )\0\n" + "\0\0\0\0\0(   )\0\0\n" + "\0\0\0\0\0\0`-'\0\0\0\n";
        String cloud3 = "\0\0\0\0.--\0\0\0\n" + "\0.+(   )\0\0\n" + "\0(   .  )\0\n" + "(   (   ))\n" + "\0`- __.'\0\0\n";
        String cloud4 = "\0\0\0\0_\0\0\0\0\0\0\0\n" + "\0\0(`  ).\0\0\0\0\n" + "\0(     ).\0\0\0\n" + "\0(       '`.\n" + "(      .   )\n" + "\0(..__.:'-'\0\n";
        decorativeClouds = new AnimatedCLIComponent[20];
        int maxSpeed = 2, minSpeed = 1;
        for (int n = 0; n < decorativeClouds.length; n++) {

            String[] rows = null;
            switch(n % 4) {

                case 0:
                    rows = cloud1.split("\n");
                    break;
                case 1:
                    rows = cloud2.split("\n");
                    break;
                case 2:
                    rows = cloud3.split("\n");
                    break;
                case 3:
                    rows = cloud4.split("\n");
                    break;
            }

            AnimatedCLIComponent decorativeCloud = new AnimatedCLIComponent(rows[0].length(), rows);
            decorativeCloud.setColor(WHITE + BLUE_BACKGROUND_BRIGHT);
            decorativeCloud.setSpeedX((float) ((Math.random() * (maxSpeed - minSpeed) + minSpeed) * (Math.random() >= 0.5 ? 1 : -1)));
            decorativeClouds[n] = decorativeCloud;
        }

        //build island components
        islands = new IslandCLIComponent[12];
        for(int n = 0; n < islands.length; n++) islands[n] = new IslandCLIComponent(n + 1);

        //build cloud components
        clouds = new ArrayList<>(playerUsernames.length);
        for(int n = 0; n < playerUsernames.length; n++) clouds.add(new CloudCLIComponent(n + 1));

        //build player dashboard components
        players = new ArrayList<>(playerUsernames.length);
        for(int n = 0; n < playerUsernames.length; n++) {

            String ansiColor;
            switch(playerTeams[n]) {

                case WHITE:
                    ansiColor = TEAM_WHITE_COLOR;
                    break;
                case BLACK:
                    ansiColor = TEAM_BLACK_COLOR;
                    break;
                case GRAY:
                    ansiColor = TEAM_GRAY_COLOR;
                    break;
                default:
                    throw new RuntimeException("Invalid team");
            }
            this.players.add(new PlayerStatusCLIComponent(n, playerUsernames[n], ansiColor));
        }

        //build text area components
        hintTextArea = new TextAreaCLIComponent(players.get(0).getWidth(), 15, "Hints");
        effectTextArea = new TextAreaCLIComponent(hintTextArea.getWidth(), hintTextArea.getHeight(), "Effect");

        setComponentsPositions();

        //set first state
        setState(new ViewOnly(cli, this));
    }
    
    /**
     * Sets every component to its starting position
     */
    private void setComponentsPositions() {

        title.setPosition(getWidth() / 2f - title.getWidth() / 2f, 0);
        line.setPosition(0, title.getHeight());

        for(AnimatedCLIComponent decorativeCloud : decorativeClouds)
            decorativeCloud.setPosition(
                    (int) (Math.random() * getWidth() - decorativeCloud.getWidth()),
                    (int) (Math.random() * skyBackground.getHeight() - decorativeCloud.getHeight() + 1));

        int offsetY = title.getHeight() + 2;

        players.get(0).setPosition(0, offsetY);
        players.get(1).setPosition(getWidth() - players.get(0).getWidth(), offsetY);
        if(players.size() >= 3) players.get(2).setPosition(0, offsetY + players.get(0).getHeight() + 1);
        if(players.size() >= 4) players.get(3).setPosition(getWidth() - players.get(0).getWidth(), offsetY + players.get(0).getHeight() + 1);

        int textAreasY = players.get(players.size() - 1).getFrameY() + players.get(0).getHeight() + 1;
        hintTextArea.setPosition(getWidth() - hintTextArea.getWidth(), textAreasY);
        effectTextArea.setPosition(0, textAreasY);

        islands[0].setPosition(2 + getWidth() / 2f, offsetY);
        islands[1].setPosition(2 + getWidth() / 2f + islands[0].getWidth(), offsetY + islands[0].getHeight() / 2f);
        islands[2].setPosition(2 + getWidth() / 2f + islands[0].getWidth() * 2, offsetY + islands[0].getHeight());
        islands[3].setPosition(2 + getWidth() / 2f + islands[0].getWidth() * 2, offsetY + islands[0].getHeight() * 2 + 1);
        islands[4].setPosition(2 + getWidth() / 2f + islands[0].getWidth(), offsetY + islands[0].getHeight() * 2 + islands[0].getHeight() / 2f + 1);
        islands[5].setPosition(2 + getWidth() / 2f, offsetY + islands[0].getHeight() * 3 + 1);
        islands[6].setPosition(-1 + getWidth() / 2f - islands[0].getWidth(), offsetY + islands[0].getHeight() * 3 + 1);
        islands[7].setPosition(-1 + getWidth() / 2f - islands[0].getWidth() * 2, offsetY + islands[0].getHeight() * 2 + islands[0].getHeight() / 2f + 1);
        islands[8].setPosition(-1 + getWidth() / 2f - islands[0].getWidth() * 3, offsetY + islands[0].getHeight() * 2 + 1);
        islands[9].setPosition(-1 + getWidth() / 2f - islands[0].getWidth() * 3, offsetY + islands[0].getHeight());
        islands[10].setPosition(-1 + getWidth() / 2f - islands[0].getWidth() * 2, offsetY + islands[0].getHeight() / 2f);
        islands[11].setPosition(-1 + getWidth() / 2f - islands[0].getWidth(), offsetY);

        //arrange clouds in a row in the middle of the islands
        int cloudsY = islands[2].getFrameY() + islands[2].getHeight() - clouds.get(0).getHeight() / 2;
        for(int n = 0; n < clouds.size(); n++) clouds.get(n).setPosition(
                getWidth() / 2f - (clouds.get(0).getWidth() * clouds.size() + clouds.size() - 2) / 2f + (clouds.get(0).getWidth() + 1) * n, cloudsY);
    }

    /**
     * Updates the position of decorative clouds in order to make them appear on the other side of the terminal window when they are out of view
     */
    private void updateDecorativeCloudsPositions() {

        for (AnimatedCLIComponent decorativeCloud : decorativeClouds) {

            if (decorativeCloud.getX() < - decorativeCloud.getWidth()) decorativeCloud.setX(getWidth());
            else if (decorativeCloud.getX() > getWidth()) decorativeCloud.setX(- decorativeCloud.getWidth());
        }
    }

    private void initializeHelpers(HelperCard[] helperCards) {

        helpers = new ArrayList<>(helperCards.length);
        for(HelperCard card : helperCards)
            helpers.add(new HelperCardCLIComponent(card.index, card.priority, card.movement));

        //arrange helper cards in a row at the bottom of the screen
        for(int n = 0; n < helpers.size(); n++) helpers.get(n).setPosition(
                getWidth() / 2f - (helpers.get(0).getWidth() * helpers.size() + helpers.size() - 2) / 2f + (helpers.get(0).getWidth() + 1) * n,
                getHeight() - helpers.get(0).getHeight() - 1);
    }

    private void initializeCharacters(CharacterCard[] characterCards) {

        characters = new CharacterCardCLIComponent[characterCards.length];
        for(int n = 0; n < characterCards.length; n++) {

            CharacterCard card = characterCards[n];
            characters[n] = new CharacterCardCLIComponent(card.index, card.effect, card.getCost());
        }

        //arrange character cards in a row below the islands
        int charactersY = islands[5].getFrameY() + islands[5].getHeight() + 2;
        for(int n = 0; n < characters.length; n++) characters[n].setPosition(
                getWidth() / 2f - (characters[0].getWidth() * characters.length + characters.length - 2) / 2f + (characters[0].getWidth() + 1) * n, charactersY);

    }

    @Override
    public void printToFrame(Frame frame) {

        updateDecorativeCloudsPositions();
        
        skyBackground.printToFrame(frame);
        for(AnimatedCLIComponent decorativeCloud : decorativeClouds) decorativeCloud.printToFrame(frame);
        title.printToFrame(frame);
        line.printToFrame(frame);
        for(IslandCLIComponent island : islands) island.printToFrame(frame);
        for(CloudCLIComponent cloud : clouds) cloud.printToFrame(frame);
        for(PlayerStatusCLIComponent player : players) player.printToFrame(frame);
        if(helpers != null) for(HelperCardCLIComponent helper : helpers) helper.printToFrame(frame);
        if(characters != null) for(CharacterCardCLIComponent character : characters) character.printToFrame(frame);
        hintTextArea.printToFrame(frame);
        effectTextArea.printToFrame(frame);
        if(prompt != null) prompt.printToFrame(frame);
    }

    /**
     * Applies the given island change to this scene
     * @param change the change to apply
     */
    public void applyChange(IslandChange change) {

        IslandCLIComponent island = islands[change.islandTileIndex];

        for(PawnColor color : PawnColor.values()) {

            OptionalInt number = change.getStudents(color);
            if(number.isPresent()) island.setStudents(color, number.getAsInt());
        }

        Optional<Boolean> tower = change.hasTower();
        if(tower.isPresent()) island.setTower(tower.get());

        Optional<Boolean> mother = change.hasMotherNature();
        if(mother.isPresent()) island.setMother(mother.get());
    }

    /**
     * Applies the given game init change to this scene
     * @param change the change to apply
     */
    public void applyChange(GameInitChange change) {

        initializeHelpers(change.getHelperCards());
        initializeCharacters(change.getCharacterCards());
    }

    public void setPrompt(CLIComponent prompt) {

        this.prompt = prompt;
    }

    public IslandCLIComponent getIsland(int index) {

        return islands[index];
    }

    public int getNumberOfIslands() {

        return islands.length;
    }

    public CloudCLIComponent getCloud(int index) {

        return clouds.get(index);
    }

    public int getNumberOfClouds() {

        return clouds.size();
    }

    public HelperCardCLIComponent getHelper(int index) {

        return helpers.get(index);
    }

    public int getNumberOfHelpers() {

        return helpers.size();
    }

    public CharacterCardCLIComponent getCharacter(int index) {

        return characters[index];
    }

    public int getNumberOfCharacters() {

        return characters.length;
    }

    public TextAreaCLIComponent getHintTextArea() {

        return hintTextArea;
    }

    public TextAreaCLIComponent getEffectTextArea() {

        return effectTextArea;
    }
}
