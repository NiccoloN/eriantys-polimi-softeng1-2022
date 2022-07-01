package it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.components;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.GameController;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.Images;
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

/**
 * This class represents a character gui component. A character gui component is associated to a javafx Group that represents a character
 * card and to a javafx TextArea, used to visualize the effect of the character on screen. This component manages various graphic elements
 * to represent the current state of the card and attaches listeners to them in order to detect inputs and react accordingly
 * @author Emanuele Musto
 */
public class CharacterGUIComponent {
    
    private final GameController gameController;
    private final Group characterGroup;
    private final ImageView characterImageView;
    private final GridPane characterCard;
    private final EventHandler<MouseEvent> characterClicked;
    private int characterIndex;
    private String effect;
    private int cost;
    private List<ColoredPawnImageView> studentsImages;
    private List<ImageView> denyTilesImages;
    private boolean studentsInitialized = false;
    private boolean denyCardsInitialized = false;
    private boolean coinInitialized = false;
    private MoveRequestMessage requestMessage;
    
    /**
     * Constructs a character gui component
     * @param characterGroup  the javafx group associated to this component
     * @param effectsTextArea the javafx text area associated to this component
     * @param gameController  the game controller associated to this component
     */
    public CharacterGUIComponent(Group characterGroup, TextArea effectsTextArea, GameController gameController) {
        
        this.gameController = gameController;
        this.characterGroup = characterGroup;
        characterImageView = (ImageView) characterGroup.getChildren().get(0);
        characterCard = (GridPane) characterGroup.getChildren().get(1);
        characterCard.setVisible(true);
        
        characterGroup.setVisible(true);
        
        characterCard.addEventHandler(MouseEvent.MOUSE_ENTERED, (event) -> effectsTextArea.setText(effect + "\n\nCost: " + cost));
        characterCard.addEventHandler(MouseEvent.MOUSE_EXITED, (event) -> effectsTextArea.setText(""));
        
        characterClicked = mouseEvent -> {
            
            try {
                manageInput(mouseEvent);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        };
    }
    
    /**
     * Makes this component stop listening to click inputs
     */
    public void stopListeningToInput() {
        
        characterCard.removeEventHandler(MouseEvent.MOUSE_CLICKED, characterClicked);
        characterGroup.setEffect(null);
    }
    
    /**
     * Sets the character card this component should represent
     * @param card the card to represent
     */
    public void setCharacter(CharacterCard card) {
        
        if (characterIndex == 0) {
            
            characterIndex = card.index;
            characterImageView.setImage(Images.charactersImages.get(characterIndex));
            effect = card.effect;
        }
        
        cost = card.getCost();
        if (card.isCostIncremented()) setCoin();
        
        if (!studentsInitialized) initializeStudents();
        if (!denyCardsInitialized) initializeDenyCards();
        
        if (card.getDenyTilesNumber() > 9) throw new InvalidParameterException("Counter must be <= 9");
        setDenyTiles(card.getDenyTilesNumber());
        
        if (characterIndex == 1 || characterIndex == 7 || characterIndex == 11) {
            
            for (ColoredPawnImageView image : studentsImages) image.clearColor();
            for (PawnColor color : PawnColor.values()) setStudents(card.countStudents(color), color);
        }
    }
    
    /**
     * Makes this component start listening to click inputs
     * @param requestMessage the message that requested to listen to inputs
     */
    public void listenToInput(MoveRequestMessage requestMessage) {
        
        this.requestMessage = requestMessage;
        characterCard.addEventHandler(MouseEvent.MOUSE_CLICKED, characterClicked);
        characterGroup.setEffect(gameController.getBorderGlowEffect());
    }
    
    private void manageInput(MouseEvent mouseEvent) throws IOException {
        
        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            
            EriantysClient.getInstance().sendToServer(new PerformedMoveMessage(requestMessage, new ChooseCharacterCard(characterIndex)));
            for (CharacterGUIComponent character : gameController.getCharacterGUIComponents())
                character.stopListeningToInput();
        }
    }
    
    private void setCoin() {
        
        if (!coinInitialized) {
            
            SizedImageView coin = new SizedImageView(Images.COIN_SIZE, Images.coinImage);
            
            characterCard.add(coin, 1, 0);
            coin.setTranslateY(-23);
        }
        coinInitialized = true;
    }
    
    private void initializeStudents() {
        
        if (characterIndex == 1 || characterIndex == 7 || characterIndex == 11) {
            
            studentsImages = new ArrayList<>(4);
            
            ColoredPawnImageView firstStudentImage = new ColoredPawnImageView(Images.STUDENT_SIZE);
            ColoredPawnImageView secondStudentImage = new ColoredPawnImageView(Images.STUDENT_SIZE);
            ColoredPawnImageView thirdStudentImage = new ColoredPawnImageView(Images.STUDENT_SIZE);
            ColoredPawnImageView fourthStudentImage = new ColoredPawnImageView(Images.STUDENT_SIZE);
            
            studentsImages.add(firstStudentImage);
            studentsImages.add(secondStudentImage);
            studentsImages.add(thirdStudentImage);
            studentsImages.add(fourthStudentImage);
        }
        
        switch (characterIndex) {
            
            case 1:
            case 11:
                characterCard.add(studentsImages.get(0), 1, 1);
                characterCard.add(studentsImages.get(1), 2, 2);
                characterCard.add(studentsImages.get(2), 1, 3);
                characterCard.add(studentsImages.get(3), 0, 2);
                
                for (ColoredPawnImageView image : studentsImages) image.setTranslateX(2);
                break;
            case 7:
                ColoredPawnImageView fifthStudentImage = new ColoredPawnImageView(Images.STUDENT_SIZE);
                ColoredPawnImageView sixthStudentImage = new ColoredPawnImageView(Images.STUDENT_SIZE);
                
                studentsImages.add(fifthStudentImage);
                studentsImages.add(sixthStudentImage);
                
                characterCard.add(studentsImages.get(0), 0, 1);
                characterCard.add(studentsImages.get(1), 2, 1);
                characterCard.add(studentsImages.get(2), 0, 2);
                characterCard.add(studentsImages.get(3), 2, 2);
                characterCard.add(studentsImages.get(4), 0, 3);
                characterCard.add(studentsImages.get(5), 2, 3);
                
                for (ColoredPawnImageView image : studentsImages) image.setTranslateX(2);
                break;
        }
        
        studentsInitialized = true;
    }
    
    private void initializeDenyCards() {
        
        if (characterIndex == 5) {
            
            denyTilesImages = new ArrayList<>(4);
            
            ImageView denyTile1 = new SizedImageView(Images.DENY_TILE_SIZE, Images.denyTileImage);
            ImageView denyTile2 = new SizedImageView(Images.DENY_TILE_SIZE, Images.denyTileImage);
            ImageView denyTile3 = new SizedImageView(Images.DENY_TILE_SIZE, Images.denyTileImage);
            ImageView denyTile4 = new SizedImageView(Images.DENY_TILE_SIZE, Images.denyTileImage);
            
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
    
    private void setDenyTiles(int numberOfDenyTiles) {
        
        if (characterIndex == 5) {
            
            for (ImageView image : denyTilesImages) image.setVisible(false);
            for (int i = 0; i < numberOfDenyTiles; i++) denyTilesImages.get(i).setVisible(true);
        }
    }
    
    private void setStudents(int number, PawnColor color) {
        
        for (int i = 0; i < number; i++) {
            
            for (ColoredPawnImageView image : studentsImages) {
                
                if (image.getColor() == null) {
                    image.setStudentOfColor(color);
                    break;
                }
            }
        }
    }
}
