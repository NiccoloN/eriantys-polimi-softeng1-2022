package it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.components;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.GameController;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.ImageFactory;
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

public class HelpersGUIComponent {
    
    private final GameController gameController;
    private final GridPane helpers;
    private final Map<Group, Integer> helpersIndices;
    private final Map<Integer, EventHandler<MouseEvent>> cardClickListeners;
    private MoveRequestMessage requestMessage;
    
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
    
    public void setRemainingHelpers(List<HelperCard> helperCards) {
        
        for (int n = 0; n < helpers.getChildren().size(); n++) {
            
            Group helper = (Group) helpers.getChildren().get(n);
            
            if (n < helperCards.size()) {
                
                HelperCard helperCard = helperCards.get(n);
                
                ImageView helperImageView = (ImageView) helper.getChildren().get(0);
                helperImageView.setImage(ImageFactory.helpersImages.get(helperCard.index));
                
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
