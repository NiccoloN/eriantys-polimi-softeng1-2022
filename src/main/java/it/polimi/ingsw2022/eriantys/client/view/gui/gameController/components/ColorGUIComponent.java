package it.polimi.ingsw2022.eriantys.client.view.gui.gameController.components;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.client.view.gui.gameController.GUIGamePhase;
import it.polimi.ingsw2022.eriantys.client.view.gui.gameController.GameController;
import it.polimi.ingsw2022.eriantys.messages.toClient.MoveRequestMessage;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ColorGUIComponent {

    private GameController controller;

    private GridPane colorButtons;

    private final Map<PawnColor, ImageView> imageByColor;
    private final Map<PawnColor, EventHandler<MouseEvent>> colorClickListeners;

    private MoveRequestMessage requestMessage;
    private int characterIndex;
    private GUIGamePhase gamePhase;
    private List<PawnColor> availableColors;


    public ColorGUIComponent(GridPane colorButtons, GameController controller) {

        this.controller = controller;
        this.colorButtons = colorButtons;
        imageByColor = new HashMap<>(5);
        colorClickListeners = new HashMap<>(10);

        int i = 0;
        for(PawnColor color : PawnColor.values()) {

            imageByColor.put(color, (ImageView) this.colorButtons.getChildren().get(i));
            
            colorClickListeners.put(color, mouseEvent -> {

                try {
                    manageInput(mouseEvent, color);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            i++;
        }
    }

    public void setAvailableColors(List<PawnColor> colors) {

        availableColors = colors;

        for(PawnColor color : PawnColor.values()) {

            imageByColor.get(color).setVisible(colors.contains(color));
        }
    }

    public void listenToInput(MoveRequestMessage requestMessage, GUIGamePhase gamePhase) {

        colorButtons.setVisible(true);
        this.requestMessage = requestMessage;
        this.gamePhase = gamePhase;
        this.characterIndex = 0;

        for(PawnColor color : availableColors) {

            imageByColor.get(color).addEventHandler(MouseEvent.MOUSE_CLICKED, colorClickListeners.get(color));
        }
    }

    public void listenToInput(MoveRequestMessage requestMessage, GUIGamePhase gamePhase, int characterIndex) {

        listenToInput(requestMessage, gamePhase);
        this.characterIndex = characterIndex;
    }

    public void stopListeningToInput() {

        colorButtons.setVisible(false);

        for(PawnColor color : availableColors) {

            imageByColor.get(color).removeEventHandler(MouseEvent.MOUSE_CLICKED, colorClickListeners.get(color));
        }
    }

    private void manageInput(MouseEvent mouseEvent, PawnColor color) throws IOException {

        if(mouseEvent.getButton() == MouseButton.PRIMARY) {

            EriantysClient client = EriantysClient.getInstance();

            switch(gamePhase) {

                case MOVE_STUDENT_ENTRANCE:
                    controller.setChosenColor(color);
                    break;
                    case
            }

            stopListeningToInput();
        }
    }
}
