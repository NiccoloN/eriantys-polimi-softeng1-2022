package it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.client.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.client.view.cli.Frame;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.CLIScene;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.components.AnimatedCLIComponent;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.components.BasicCLIComponent;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.components.CLIComponent;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.components.TextAreaCLIComponent;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.components.*;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.components.player.PlayerStatusCLIComponent;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.states.ViewOnly;
import it.polimi.ingsw2022.eriantys.messages.toClient.changes.*;
import it.polimi.ingsw2022.eriantys.server.controller.Mode;
import it.polimi.ingsw2022.eriantys.server.model.board.IslandTile;
import it.polimi.ingsw2022.eriantys.server.model.cards.CharacterCard;
import it.polimi.ingsw2022.eriantys.server.model.cards.HelperCard;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;
import it.polimi.ingsw2022.eriantys.server.model.players.Player;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static it.polimi.ingsw2022.eriantys.client.view.cli.AnsiCodes.*;

/**
 * This class represents a game scene
 * @author Niccolò Nicolosi
 */
public class GameScene extends CLIScene {

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
    private ColorCLIComponent[] colors;

    /**
     * Constructs a game scene
     * @param cli the cli to associate to this scene
     * @param width the width of this scene
     * @param height the height of this scene
     * @param players the players of the game
     * @param gameMode the mode of the game
     */
    public GameScene(EriantysCLI cli, int width, int height, Player[] players, Mode gameMode) {

        super(cli, width, height);

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
        clouds = new ArrayList<>(players.length);
        for(int n = 0; n < players.length; n++) clouds.add(new CloudCLIComponent(n + 1));

        //build player dashboard components
        this.players = new ArrayList<>(players.length);
        for (int n = 0; n < players.length; n++) this.players.add(new PlayerStatusCLIComponent(players[n].username, players[n].team.ansiColor, n % 2 != 0));

        //build text area components
        hintTextArea = new TextAreaCLIComponent(this.players.get(0).getWidth(), 15, "Hints");
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

    private void setHelpers(HelperCard[] helperCards) {

        if (helpers == null) helpers = new ArrayList<>(helperCards.length);
        else helpers.clear();

        for(HelperCard card : helperCards) helpers.add(new HelperCardCLIComponent(card.index, card.priority, card.movement));

        //arrange helper cards in a row at the bottom of the screen
        for(int n = 0; n < helpers.size(); n++) helpers.get(n).setPosition(
                getWidth() / 2f - (helpers.get(0).getWidth() * helpers.size() + helpers.size() - 2) / 2f + (helpers.get(0).getWidth() + 1) * n,
                getHeight() - helpers.get(0).getHeight() - 1);
    }

    private void setCharacters(CharacterCard[] characterCards) {

        if (characters == null) characters = new CharacterCardCLIComponent[characterCards.length];

        for(int n = 0; n < characterCards.length; n++) {

            CharacterCard card = characterCards[n];
            characters[n] = new CharacterCardCLIComponent(card.index, card.effect, card.getCost());
        }

        //arrange character cards in a row below the islands
        int charactersY = islands[5].getFrameY() + islands[5].getHeight() + 2;
        for(int n = 0; n < characters.length; n++) characters[n].setPosition(
                getWidth() / 2f - (characters[0].getWidth() * characters.length + characters.length - 2) / 2f + (characters[0].getWidth() + 1) * n, charactersY);

    }

    public void setColors(List<PawnColor> colors) {

        this.colors = new ColorCLIComponent[colors.size()];
        for(int n = 0; n < colors.size(); n++) this.colors[n] = new ColorCLIComponent(colors.get(n));

        //arrange colors in a row at the bottom of the screen
        for(int n = 0; n < this.colors.length; n++) this.colors[n].setPosition(
                getWidth() / 2f - (this.colors[0].getWidth() * this.colors.length + this.colors.length - 2) / 2f + (this.colors[0].getWidth() + 1) * n,
                getHeight() - this.colors[0].getHeight() - 3);
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
        if(colors != null) for(BasicCLIComponent color : colors) color.printToFrame(frame);
        hintTextArea.printToFrame(frame);
        effectTextArea.printToFrame(frame);
        if(prompt != null) prompt.printToFrame(frame);
    }

    /**
     * Applies the given character cards change to this scene
     * @param change the change to apply
     */
    public void applyChange(CharacterCardsChange change) {

        setCharacters(change.getCharacterCards());
    }

    /**
     * Applies the given helper cards change to this scene
     * @param change the change to apply
     */
    public void applyChange(HelperCardsChange change) {

        setHelpers(change.getHelperCards());
    }

    /**
     * Applies the given island change to this scene
     * @param change the change to apply
     */
    public void applyChange(IslandChange change) {

        AtomicInteger islandTileIndex = new AtomicInteger();

        Arrays.stream(islands).filter((x) -> x.getIndex() == change.compoundIslandIndex)
                .forEach((island) -> {

                    IslandTile islandTile = change.getIslandTile(islandTileIndex.getAndIncrement());

                    for(PawnColor color : PawnColor.values()) island.setStudents(color, islandTile.countStudents(color));
                    island.setMother(islandTile.hasMotherNature());
                    island.setTower(islandTile.hasTower());

                    String teamColor = change.getTeam().isPresent() ? change.getTeam().get().ansiColor : RESET;
                    island.setTeamColor(teamColor);
                });
    }

    /**
     * Applies the given cloud change to this scene
     * @param change the change to apply
     */
    public void applyChange(CloudChange change) {

        CloudCLIComponent cloud = clouds.get(change.cloudIndex - 1);
        for(PawnColor color : PawnColor.values()) cloud.setStudents(color, change.getStudents(color));
    }

    /**
     * Applies the given school change to this scene
     * @param change the change to apply
     * @throws NoSuchElementException if no player of the given username is found in this game scene
     */
    public void applyChange(SchoolChange change) {

        PlayerStatusCLIComponent player = players.stream()
                .filter((x) -> x.getNickname().equals(change.getPlayerUsername())).findAny().orElseThrow();

        for (PawnColor color : PawnColor.values()) {

            player.setEntranceStudents(color, change.getEntranceStudents(color));
            player.setTableStudents(color, change.getTableStudents(color));
            player.setProfessor(color, change.hasProfessor(color));
            player.setTowers(change.getTowers());
        }
    }

    /**
     * Applies the given player change to this scene
     * @param change the change to apply
     * @throws NoSuchElementException if no player of the given username is found in this game scene
     */
    public void applyChange(PlayerChange change) {

        PlayerStatusCLIComponent player = players.stream()
                .filter((x) -> x.getNickname().equals(change.getUsername())).findAny().orElseThrow();

        player.setCoins(change.getCoins());
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

        return helpers != null ? helpers.size() : 0;
    }

    public CharacterCardCLIComponent getCharacter(int index) {

        return characters[index];
    }

    public int getNumberOfCharacters() {

        return characters != null ? characters.length : 0;
    }

    public ColorCLIComponent getColor(int index) {

        return colors[index];
    }

    public int getNumberOfColors() {

        return colors != null ? colors.length : 0;
    }

    public TextAreaCLIComponent getHintTextArea() {

        return hintTextArea;
    }

    public TextAreaCLIComponent getEffectTextArea() {

        return effectTextArea;
    }

    public PlayerStatusCLIComponent getPlayer() {

        return players.stream().filter((x) -> x.getNickname().equals(EriantysClient.getInstance().getUsername())).findAny().orElseThrow();
    }
}
