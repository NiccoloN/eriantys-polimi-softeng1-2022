package it.polimi.ingsw2022.eriantys.client.view.gui.gameController.components;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.client.view.gui.gameController.ImageFactory;
import it.polimi.ingsw2022.eriantys.messages.moves.ChooseHelperCard;
import it.polimi.ingsw2022.eriantys.messages.toClient.MoveRequestMessage;
import it.polimi.ingsw2022.eriantys.messages.toServer.PerformedMoveMessage;
import it.polimi.ingsw2022.eriantys.server.model.cards.HelperCard;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HelpersGUIComponent {

    private final GridPane helpers;
    private final Map<ImageView, Integer> imagesIndices;
    private final Map<Integer, EventHandler<MouseEvent>> cardClickListeners;
    private MoveRequestMessage requestMessage;

    public HelpersGUIComponent(GridPane helpers) {

        this.helpers = helpers;
        imagesIndices = new HashMap<>(10);

        cardClickListeners = new HashMap<>(10);
        for(int n = 1; n <= 10; n++) {

            int cardIndex = n;
            cardClickListeners.put(n, mouseEvent -> {

                try {
                    manageInput(mouseEvent, cardIndex);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public void setRemainingHelpers(List<HelperCard> helperCards) {

        for(int n = 0; n < helpers.getChildren().size(); n++) {

            ImageView helper = ((ImageView) helpers.getChildren().get(n));

            if(n < helperCards.size()) {

                HelperCard helperCard = helperCards.get(n);

                imagesIndices.put(helper, helperCard.index);
                helper.setImage(ImageFactory.helpersImages.get(helperCard.index));
                helper.setVisible(true);
            }

            else {

                imagesIndices.put(helper, 0);
                helper.setVisible(false);
            }
        }

        helpers.setTranslateX((helpers.getChildren().get(0).getLayoutBounds().getWidth() + 4) / 2 *
                              (helpers.getColumnCount() - helperCards.size()));
    }

    public void listenToInput(MoveRequestMessage requestMessage) {

        helpers.setVisible(true);
        this.requestMessage = requestMessage;

        for(int n = 0; n < helpers.getChildren().size(); n++) {

            ImageView helper = (ImageView) helpers.getChildren().get(n);
            int cardIndex = imagesIndices.get(helper);
            if(cardIndex > 0) helper.addEventHandler(MouseEvent.MOUSE_CLICKED, cardClickListeners.get(cardIndex));
        }
    }

    public void stopListeningToInput() {

        helpers.setVisible(false);

        for(int n = 0; n < helpers.getChildren().size(); n++) {

            ImageView helper = (ImageView) helpers.getChildren().get(n);
            int cardIndex = imagesIndices.get(helper);
            if(cardIndex > 0) helper.removeEventHandler(MouseEvent.MOUSE_CLICKED, cardClickListeners.get(cardIndex));
        }
    }

    private void manageInput(MouseEvent mouseEvent, int cardIndex) throws IOException {

        if(mouseEvent.getButton() == MouseButton.PRIMARY) {

            EriantysClient.getInstance().sendToServer(new PerformedMoveMessage(requestMessage, new ChooseHelperCard(cardIndex)));
            stopListeningToInput();
        }
    }
}
