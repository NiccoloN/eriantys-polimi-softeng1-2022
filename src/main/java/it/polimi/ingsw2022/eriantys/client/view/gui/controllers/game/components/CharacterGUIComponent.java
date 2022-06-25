package it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.components;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.ImageFactory;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.utilityNodes.ColoredPawnImageView;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.utilityNodes.SizedImageView;
import it.polimi.ingsw2022.eriantys.messages.moves.ChooseCharacterCard;
import it.polimi.ingsw2022.eriantys.messages.toClient.MoveRequestMessage;
import it.polimi.ingsw2022.eriantys.messages.toServer.PerformedMoveMessage;
import it.polimi.ingsw2022.eriantys.server.model.cards.CharacterCard;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

public class CharacterGUIComponent {

    private boolean studentsInitialized = false;
    private boolean denyCardsInitialized = false;
    private boolean coinInitialized = false;

    private int characterIndex;
    private String effect;
    private int cost;
    private int numberOfDenyTiles;

    private final ImageView characterImage;
    private final GridPane characterCard;
    private final TextArea effectsTextArea;

    private List<ImageView> denyTilesImages;
    private List<ColoredPawnImageView> studentsImages;
    private SizedImageView coin;

    private final EventHandler<MouseEvent> characterClicked;
    private MoveRequestMessage requestMessage;

    public CharacterGUIComponent(Group group, TextArea effectsTextArea) {

        characterImage = (ImageView) group.getChildren().get(0);
        characterCard = (GridPane) group.getChildren().get(1);
        characterCard.setVisible(true);
        this.effectsTextArea = effectsTextArea;

        characterCard.addEventHandler(MouseEvent.MOUSE_ENTERED, (event) -> {

            this.effectsTextArea.setText(effect + "\n\nCost: " + cost);
        });

        characterCard.addEventHandler(MouseEvent.MOUSE_EXITED, (event) -> {

            this.effectsTextArea.setText("");
        });

        characterClicked = mouseEvent -> {

            try {
                manageInput(mouseEvent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }

    public void setCharacter(CharacterCard card) {

        if(characterIndex == 0) {

            characterIndex = card.index;
            characterImage.setImage(ImageFactory.charactersImages.get(characterIndex));
            effect = card.effect;
        }

        cost = card.getCost();
        if(card.isCostIncremented()) setCoin();

        if(!studentsInitialized) initializeStudents();
        if(!denyCardsInitialized) initializeDenyCards();

        if(card.getDenyTilesNumber() > 9) throw new InvalidParameterException("Counter must be <= 9");
        numberOfDenyTiles = card.getDenyTilesNumber();
        setDenyTiles(numberOfDenyTiles);

        if(characterIndex == 1 || characterIndex == 7 || characterIndex == 11) {

            for(ColoredPawnImageView image : studentsImages) image.clearColor();
            for(PawnColor color : PawnColor.values()) setStudents(card.countStudents(color), color);
        }
    }

    private void initializeStudents() {

        if(characterIndex == 1 || characterIndex == 7 || characterIndex == 11) {

            studentsImages = new ArrayList<>(4);

            ColoredPawnImageView firstStudentImage = new ColoredPawnImageView(ImageFactory.STUDENT_SIZE);
            ColoredPawnImageView secondStudentImage = new ColoredPawnImageView(ImageFactory.STUDENT_SIZE);
            ColoredPawnImageView thirdStudentImage = new ColoredPawnImageView(ImageFactory.STUDENT_SIZE);
            ColoredPawnImageView fourthStudentImage = new ColoredPawnImageView(ImageFactory.STUDENT_SIZE);

            studentsImages.add(firstStudentImage);
            studentsImages.add(secondStudentImage);
            studentsImages.add(thirdStudentImage);
            studentsImages.add(fourthStudentImage);
        }

        switch(characterIndex) {

            case 1:
            case 11:
                characterCard.add(studentsImages.get(0), 1, 1);
                characterCard.add(studentsImages.get(1), 2, 2);
                characterCard.add(studentsImages.get(2), 1, 3);
                characterCard.add(studentsImages.get(3), 0, 2);

                for(ColoredPawnImageView image : studentsImages) image.setTranslateX(2);
                break;
            case 7:
                ColoredPawnImageView fifthStudentImage = new ColoredPawnImageView(ImageFactory.STUDENT_SIZE);
                ColoredPawnImageView sixthStudentImage = new ColoredPawnImageView(ImageFactory.STUDENT_SIZE);

                studentsImages.add(fifthStudentImage);
                studentsImages.add(sixthStudentImage);

                characterCard.add(studentsImages.get(0), 0, 1);
                characterCard.add(studentsImages.get(1), 2, 1);
                characterCard.add(studentsImages.get(2), 0, 2);
                characterCard.add(studentsImages.get(3), 2, 2);
                characterCard.add(studentsImages.get(4), 0, 3);
                characterCard.add(studentsImages.get(5), 2, 3);

                for(ColoredPawnImageView image : studentsImages) image.setTranslateX(2);
                break;
        }

        studentsInitialized = true;
    }

    private void initializeDenyCards() {

        if(characterIndex == 5) {

            denyTilesImages = new ArrayList<>(4);

            ImageView denyTile1 = new SizedImageView(ImageFactory.DENY_TILE_SIZE, ImageFactory.denyTileImage);
            ImageView denyTile2 = new SizedImageView(ImageFactory.DENY_TILE_SIZE, ImageFactory.denyTileImage);
            ImageView denyTile3 = new SizedImageView(ImageFactory.DENY_TILE_SIZE, ImageFactory.denyTileImage);
            ImageView denyTile4 = new SizedImageView(ImageFactory.DENY_TILE_SIZE, ImageFactory.denyTileImage);

            characterCard.add(denyTile1, 1, 1);
            characterCard.add(denyTile2, 2, 2);
            characterCard.add(denyTile3, 1, 3);
            characterCard.add(denyTile4, 0, 2);

            denyTilesImages.add(denyTile1);
            denyTilesImages.add(denyTile2);
            denyTilesImages.add(denyTile3);
            denyTilesImages.add(denyTile4);
        }

        denyCardsInitialized = true;
    }

    private void setStudents(int number, PawnColor color) {

        for(int i = 0; i < number; i++) {

            for(ColoredPawnImageView image : studentsImages) {

                if(image.getColor() == null) {
                    image.setStudentOfColor(color);
                    break;
                }
            }
        }
    }

    private void setCoin() {

        if(!coinInitialized) {

            coin = new SizedImageView(ImageFactory.COIN_SIZE, ImageFactory.coinImage);

            characterCard.add(coin, 1, 0);
            coin.setTranslateY(-23);
        }
        coinInitialized = true;
    }

    private void setDenyTiles(int numberOfDenyTiles) {

        if(characterIndex == 5) {

            for(ImageView image : denyTilesImages) image.setVisible(false);
            for(int i = 0; i < numberOfDenyTiles; i++) denyTilesImages.get(i).setVisible(true);
        }
    }

    public void listenToInput(MoveRequestMessage requestMessage) {

        this.requestMessage = requestMessage;
        characterImage.addEventHandler(MouseEvent.MOUSE_CLICKED, characterClicked);
    }

    public void stopListeningToInput() {

        characterImage.removeEventHandler(MouseEvent.MOUSE_CLICKED, characterClicked);
    }

    private void manageInput(MouseEvent mouseEvent) throws IOException {

        if(mouseEvent.getButton() == MouseButton.PRIMARY) {

            EriantysClient.getInstance().sendToServer(new PerformedMoveMessage
                    (requestMessage, new ChooseCharacterCard(characterIndex)));
        }
    }
}
