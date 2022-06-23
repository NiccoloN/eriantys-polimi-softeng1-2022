package it.polimi.ingsw2022.eriantys.client.view.gui.components;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.client.view.gui.ColoredImageView;
import it.polimi.ingsw2022.eriantys.messages.moves.ChooseCloud;
import it.polimi.ingsw2022.eriantys.messages.toClient.MoveRequestMessage;
import it.polimi.ingsw2022.eriantys.messages.toServer.PerformedMoveMessage;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CloudGUIComponent {

    private final int gridPaneIndex = 1;
    private final int buttonIndex = 2;

    private final int cloudIndex;
    private List<ColoredImageView> students;

    GridPane cloudGrid;
    Button button;

    EventHandler<MouseEvent> button_clicked;
    MoveRequestMessage requestMessage;

    public CloudGUIComponent(int cloudIndex, Group cloud) {

        this.cloudIndex = cloudIndex;
        cloudGrid = (GridPane) cloud.getChildren().get(gridPaneIndex);
        button = (Button) cloud.getChildren().get(buttonIndex);

        initializeStudentImageViews();

        button_clicked = mouseEvent -> {

            try {
                manageInput(mouseEvent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }

    private void initializeStudentImageViews() {

        students = new ArrayList<>(3);

        ColoredImageView firstStudentImage = new ColoredImageView();
        ColoredImageView secondStudentImage = new ColoredImageView();
        ColoredImageView thirdStudentImage = new ColoredImageView();

        firstStudentImage.setVisible(false);
        secondStudentImage.setVisible(false);
        thirdStudentImage.setVisible(false);

        students.add(firstStudentImage);
        students.add(secondStudentImage);
        students.add(thirdStudentImage);

        switch(EriantysClient.getInstance().getGameSettings().numberOfPlayers) {

            case 2:
            case 4:
                cloudGrid.add(firstStudentImage, 1, 0);
                cloudGrid.add(secondStudentImage, 0, 2);
                cloudGrid.add(thirdStudentImage, 2, 2);

                firstStudentImage.setTranslateY(2);

                secondStudentImage.setTranslateX(2);
                secondStudentImage.setTranslateY(-10);

                thirdStudentImage.setTranslateX(-2);
                thirdStudentImage.setTranslateY(-10);
                break;
            case 3:
                ColoredImageView fourthStudentImage = new ColoredImageView();
                students.add(fourthStudentImage);

                fourthStudentImage.setVisible(false);

                cloudGrid.add(firstStudentImage, 0, 0);
                cloudGrid.add(secondStudentImage, 2, 0);
                cloudGrid.add(thirdStudentImage, 0, 2);
                cloudGrid.add(fourthStudentImage, 2, 2);

                firstStudentImage.setTranslateX(7);
                firstStudentImage.setTranslateY(7);

                secondStudentImage.setTranslateX(-7);
                secondStudentImage.setTranslateY(7);

                thirdStudentImage.setTranslateX(7);
                thirdStudentImage.setTranslateY(-7);

                fourthStudentImage.setTranslateX(-7);
                fourthStudentImage.setTranslateY(-7);
                break;
            default: throw new RuntimeException("Number of player needs to be between 2 and 4");
        }
    }

    public void listenToInput(MoveRequestMessage requestMessage) {

        this.requestMessage = requestMessage;
        button.addEventHandler(MouseEvent.MOUSE_CLICKED, button_clicked);
    }

    public void stopListeningToInput() {

        button.removeEventHandler(MouseEvent.MOUSE_CLICKED, button_clicked);
    }

    public void manageInput(MouseEvent mouseEvent) throws IOException {

        if(mouseEvent.isPrimaryButtonDown()) {

            EriantysClient.getInstance().sendToServer(new PerformedMoveMessage(requestMessage, new ChooseCloud(cloudIndex)));
        }
    }

    public void setStudents(PawnColor color, int numberOfStudents) {

        for(int i = 0; i < numberOfStudents; i++) {

            for(ColoredImageView image : students) {

                if(image.getColor() == null) {

                    image.setColor(color);
                    break;
                }
            }
        }
    }

    public void clearStudents() {

        for(ColoredImageView image : students) image.clearColor();
    }

    public int getCloudIndex() {
        return cloudIndex;
    }
}
