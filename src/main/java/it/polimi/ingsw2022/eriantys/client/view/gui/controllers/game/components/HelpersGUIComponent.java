package it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.components;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.GameController;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.Images;
import it.polimi.ingsw2022.eriantys.messages.moves.ChooseHelperCard;
import it.polimi.ingsw2022.eriantys.messages.toClient.MoveRequestMessage;
import it.polimi.ingsw2022.eriantys.messages.toServer.PerformedMoveMessage;
import it.polimi.ingsw2022.eriantys.server.model.cards.HelperCard;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents a helpers gui component. A helpers gui component is associated to a javafx GridPane that represents the
 * helper cards currently in the hand of the player. This component manages various graphic elements to represent the current playable
 * and not playable helper cards and attaches listeners to them in order to detect inputs and react accordingly
 * @author Niccolò Nicolosi
 */
public class HelpersGUIComponent {
    
    private final GameController gameController;
    private final GridPane helpers;
    private final Map<Group, Integer> helpersIndices;
    private final Map<Integer, EventHandler<MouseEvent>> cardClickListeners;
    private MoveRequestMessage requestMessage;
    
    /**
     * Constructs a helpers gui component
     * @param helpers        the javafx grid pane associated to this component
     * @param gameController the game controller associated to this component
     */
    public HelpersGUIComponent(GridPane helpers, GameController gameController) {
        
        this.gameController = gameController;
        this.helpers = helpers;
        
        helpersIndices = new HashMap<>(10);
        
        cardClickListeners = new HashMap<>(10);
        for (int n = 1; n <= 10; n++) {
            
            int cardIndex = n;
            cardClickListeners.put(n, mouseEvent -> {
                
                try {
                    
                    manageInput(mouseEvent, cardIndex);
                }
                catch (IOException e) {
                    
                    e.printStackTrace();
                }
            });
        }
    }
    
    /**
     * Makes this component stop listening to click inputs
     */
    public void stopListeningToInput() {
        
        helpers.setVisible(false);
        requestMessage = null;
        
        for (int n = 0; n < helpers.getChildren().size(); n++) {
            
            Group helper = (Group) helpers.getChildren().get(n);
            ImageView helperImageView = (ImageView) helper.getChildren().get(0);
            
            int cardIndex = helpersIndices.get(helper);
            
            if (cardIndex > 0)
                helperImageView.removeEventHandler(MouseEvent.MOUSE_CLICKED, cardClickListeners.get(cardIndex));
        }
    }
    
    /**
     * Sets the helper cards to represent
     * @param helperCards the list of helper cards to represent
     */
    public void setHelpers(List<HelperCard> helperCards) {
        
        for (int n = 0; n < helpers.getChildren().size(); n++) {
            
            Group helper = (Group) helpers.getChildren().get(n);
            
            if (n < helperCards.size()) {
                
                HelperCard helperCard = helperCards.get(n);
                
                ImageView helperImageView = (ImageView) helper.getChildren().get(0);
                helperImageView.setImage(Images.helpersImages.get(helperCard.index));
                
                helpersIndices.put(helper, helperCard.index);
                helper.setVisible(true);
            }
            else {
                
                helpersIndices.put(helper, 0);
                helper.setVisible(false);
            }
        }
        
        helpers.setTranslateX((helpers.getChildren().get(0).getLayoutBounds().getWidth() + 4) / 2 * (helpers.getColumnCount() - helperCards.size()));
    }
    
    /**
     * Makes this component start listening to click inputs
     * @param requestMessage    the message that requested to listen to inputs
     * @param unplayableIndices the list of indices of helper cards that can't currently be played
     */
    public void listenToInput(MoveRequestMessage requestMessage, List<Integer> unplayableIndices) {
        
        helpers.setVisible(true);
        this.requestMessage = requestMessage;
        
        for (int n = 0; n < helpers.getChildren().size(); n++) {
            
            Group helper = (Group) helpers.getChildren().get(n);
            
            int cardIndex = helpersIndices.get(helper);
            
            if (cardIndex > 0) {
                
                if (!unplayableIndices.contains(cardIndex)) {
                    
                    ImageView helperImageView = (ImageView) helper.getChildren().get(0);
                    
                    helperImageView.addEventHandler(MouseEvent.MOUSE_CLICKED, cardClickListeners.get(cardIndex));
                    helper.setEffect(gameController.getBorderGlowEffect());
                }
                else {
                    
                    ColorAdjust colorAdjust = new ColorAdjust();
                    colorAdjust.setBrightness(-0.5);
                    helper.setEffect(colorAdjust);
                }
            }
        }
    }
    
    private void manageInput(MouseEvent mouseEvent, int cardIndex) throws IOException {
        
        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            
            EriantysClient.getInstance().sendToServer(new PerformedMoveMessage(requestMessage, new ChooseHelperCard(cardIndex)));
            stopListeningToInput();
        }
    }
}
