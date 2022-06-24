package it.polimi.ingsw2022.eriantys.client.view.gui.gameController.components;

import it.polimi.ingsw2022.eriantys.client.view.gui.gameController.ImageFactory;
import it.polimi.ingsw2022.eriantys.server.model.cards.HelperCard;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.List;

public class HelpersGUIComponent {

    private final GridPane helpers;

    public HelpersGUIComponent(GridPane helpers) {

        this.helpers = helpers;
    }

    public void setRemainingHelpers(List<HelperCard> helperCards) {

        for(int n = 0; n < helpers.getChildren().size(); n++) {

            ImageView helper = ((ImageView) helpers.getChildren().get(n));
            if(n < helperCards.size()) {

                helper.setImage(ImageFactory.helpersImages.get(helperCards.get(n).index));
                helper.setVisible(true);
            }

            else helper.setVisible(false);
        }

        helpers.setTranslateX((helpers.getChildren().get(0).getLayoutBounds().getWidth() + 4) / 2 *
                              (helpers.getColumnCount() - helperCards.size()));
    }
}
