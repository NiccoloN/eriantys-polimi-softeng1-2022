package it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.components;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.GameController;
import it.polimi.ingsw2022.eriantys.messages.moves.Abort;
import it.polimi.ingsw2022.eriantys.messages.moves.ChooseColor;
import it.polimi.ingsw2022.eriantys.messages.requests.ChooseColorRequest;
import it.polimi.ingsw2022.eriantys.messages.toClient.MoveRequestMessage;
import it.polimi.ingsw2022.eriantys.messages.toServer.PerformedMoveMessage;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents a colors gui component. A colors gui component is associated to a javafx GridPane that represents the
 * selectable student colors. This component manages various graphic elements to represent the current selectable colors and
 * attaches listeners to them in order to detect inputs and react accordingly
 * @author Niccolò Nicolosi
 * @author Emanuele Musto
 */
public class ColorsGUIComponent {
    
    private final GameController gameController;
    private final GridPane colorButtons;
    
    private final Map<Button, PawnColor> buttonColors;
    private final Map<PawnColor, EventHandler<MouseEvent>> colorClickListeners;
    private final EventHandler<KeyEvent> abortEventHandler;
    
    private MoveRequestMessage requestMessage;
    private int characterIndex;
    
    /**
     * Constructs a colors gui component
     * @param colorButtons   the javafx grid pane associated to this component
     * @param gameController the game controller associated to this component
     */
    public ColorsGUIComponent(GridPane colorButtons, GameController gameController) {
        
        this.colorButtons = colorButtons;
        this.gameController = gameController;
        
        buttonColors = new HashMap<>(5);
        colorClickListeners = new HashMap<>(5);
        
        PawnColor[] pawnColors = PawnColor.values();
        for (PawnColor color : pawnColors) {
            
            colorClickListeners.put(color, (mouseEvent) -> {
                
                try {
                    
                    manageInput(mouseEvent, color);
                }
                catch (IOException e) {
                    
                    e.printStackTrace();
                }
            });
        }
        
        abortEventHandler = (keyEvent) -> {
    
            if (keyEvent.getCode() == KeyCode.ESCAPE) {
        
                try {
            
                    EriantysClient.getInstance().sendToServer(new PerformedMoveMessage(requestMessage, new Abort()));
                    stopListeningToInput();
                }
                catch (IOException e) {
            
                    e.printStackTrace();
                }
            }
        };
    }
    
    /**
     * Makes this component stop listening to click inputs
     */
    public void stopListeningToInput() {
        
        colorButtons.setVisible(false);
        requestMessage = null;
        characterIndex = 0;
        
        for (int n = 0; n < colorButtons.getChildren().size(); n++) {
            
            Button colorButton = (Button) ((Group) colorButtons.getChildren().get(n)).getChildren().get(0);
            PawnColor color = buttonColors.get(colorButton);
            if (color != null) colorButton.removeEventHandler(MouseEvent.MOUSE_CLICKED, colorClickListeners.get(color));
        }
    
        gameController.getGui().getCurrentScene().removeEventHandler(KeyEvent.KEY_PRESSED, abortEventHandler);
    }
    
    /**
     * Makes this component start listening to click inputs
     * @param requestMessage  the message that requested to listen to inputs
     * @param availableColors the selectable colors to represent
     * @param characterIndex  the index of the character card that requested to listen to inputs
     */
    public void listenToInput(MoveRequestMessage requestMessage, List<PawnColor> availableColors, int characterIndex) {
        
        listenToInput(requestMessage, availableColors);
        this.characterIndex = characterIndex;
        
        if(characterIndex == 7 || characterIndex == 10)
            gameController.getGui().getCurrentScene().addEventHandler(KeyEvent.KEY_PRESSED, abortEventHandler);
    }
    
    /**
     * Makes this component start listening to click inputs
     * @param requestMessage  the message that requested to listen to inputs
     * @param availableColors the selectable colors to represent
     */
    public void listenToInput(MoveRequestMessage requestMessage, List<PawnColor> availableColors) {
        
        gameController.getHelpersGUIComponent().stopListeningToInput();
        
        setAvailableColors(availableColors);
        
        colorButtons.setVisible(true);
        this.requestMessage = requestMessage;
        characterIndex = 0;
        
        for (int n = 0; n < colorButtons.getChildren().size(); n++) {
            
            Button colorButton = (Button) ((Group) colorButtons.getChildren().get(n)).getChildren().get(0);
            PawnColor color = buttonColors.get(colorButton);
            if (color != null) colorButton.addEventHandler(MouseEvent.MOUSE_CLICKED, colorClickListeners.get(color));
        }
    }
    
    private void setAvailableColors(List<PawnColor> colors) {
        
        for (int n = 0; n < colorButtons.getChildren().size(); n++) {
            
            Button colorButton = (Button) ((Group) colorButtons.getChildren().get(n)).getChildren().get(0);
            
            if (n < colors.size()) {
                
                PawnColor color = colors.get(n);
                
                buttonColors.put(colorButton, color);
                colorButton.getStyleClass().remove(1);
                colorButton.getStyleClass().add(color.name().toLowerCase() + "-button");
                colorButton.setVisible(true);
            }
            else {
                
                buttonColors.put(colorButton, null);
                colorButton.setVisible(false);
            }
        }
        
        colorButtons.setTranslateX((colorButtons.getChildren().get(0).getLayoutBounds().getWidth() + 5) / 2 * (colorButtons.getColumnCount() - colors.size()));
    }
    
    private void manageInput(MouseEvent mouseEvent, PawnColor color) throws IOException {
        
        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            
            if (characterIndex <= 0) {
                
                for (IslandGUIComponent island : gameController.getIslandGUIComponents())
                    island.listenToInput(requestMessage, color);
                gameController.getSchoolGUIComponentOfPlayer(EriantysClient.getInstance().getUsername()).listenToInput(requestMessage, color);
            }
            else if (characterIndex == 1) {
                
                for (IslandGUIComponent island : gameController.getIslandGUIComponents())
                    island.listenToInput(requestMessage, color, characterIndex);
            }
            else {
                
                ChooseColorRequest colorRequest = (ChooseColorRequest) requestMessage.moveRequest;
                EriantysClient.getInstance().sendToServer(new PerformedMoveMessage(requestMessage, new ChooseColor(color, characterIndex, colorRequest.fromWhere)));
            }
            
            stopListeningToInput();
        }
    }
}
