package it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.components;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.GameController;
import it.polimi.ingsw2022.eriantys.messages.toClient.MoveRequestMessage;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ColorsGUIComponent {

    private final GameController controller;
    private final GridPane colorButtons;

    private final Map<Button, PawnColor> buttonColors;
    private final Map<PawnColor, EventHandler<MouseEvent>> colorClickListeners;

    private MoveRequestMessage requestMessage;
    private int characterIndex;

    public ColorsGUIComponent(GridPane colorButtons, GameController controller) {

        this.colorButtons = colorButtons;
        this.controller = controller;

        buttonColors = new HashMap<>(5);
        colorClickListeners = new HashMap<>(5);

        PawnColor[] pawnColors = PawnColor.values();
        for(PawnColor color : pawnColors) {

            colorClickListeners.put(color, mouseEvent -> {

                try {
                    manageInput(mouseEvent, color);
                }
                catch(IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public void setAvailableColors(List<PawnColor> colors) {

        for(int n = 0; n < colorButtons.getChildren().size(); n++) {

            Button colorButton = (Button) colorButtons.getChildren().get(n);

            if(n < colors.size()) {

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

        colorButtons.setTranslateX((colorButtons.getChildren().get(0).getLayoutBounds().getWidth() + 5) / 2 *
                              (colorButtons.getColumnCount() - colors.size()));
    }

    public void listenToInput(MoveRequestMessage requestMessage, List<PawnColor> availableColors) {

        setAvailableColors(availableColors);

        colorButtons.setVisible(true);
        this.requestMessage = requestMessage;
        characterIndex = 0;

        for(int n = 0; n < colorButtons.getChildren().size(); n++) {

            Button colorButton = (Button) colorButtons.getChildren().get(n);
            PawnColor color = buttonColors.get(colorButton);
            if(color != null) colorButton.addEventHandler(MouseEvent.MOUSE_CLICKED, colorClickListeners.get(color));
        }
    }

    public void listenToInput(MoveRequestMessage requestMessage, List<PawnColor> availableColors, int characterIndex) {

        listenToInput(requestMessage, availableColors);
        this.characterIndex = characterIndex;
    }

    public void stopListeningToInput() {

        colorButtons.setVisible(false);
        requestMessage = null;
        characterIndex = 0;

        for(int n = 0; n < colorButtons.getChildren().size(); n++) {

            Button colorButton = (Button) colorButtons.getChildren().get(n);
            PawnColor color = buttonColors.get(colorButton);
            if(color != null) colorButton.removeEventHandler(MouseEvent.MOUSE_CLICKED, colorClickListeners.get(color));
        }
    }

    private void manageInput(MouseEvent mouseEvent, PawnColor color) throws IOException {

        if(mouseEvent.getButton() == MouseButton.PRIMARY) {

            EriantysClient client = EriantysClient.getInstance();

            System.out.println(color);
            controller.setChosenColor(color);


            stopListeningToInput();
        }
    }
}
