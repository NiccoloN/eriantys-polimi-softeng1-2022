package it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.components;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.GameController;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.utilityNodes.ColoredPawnImageView;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.ImageFactory;
import it.polimi.ingsw2022.eriantys.messages.moves.ChooseCloud;
import it.polimi.ingsw2022.eriantys.messages.toClient.MoveRequestMessage;
import it.polimi.ingsw2022.eriantys.messages.toServer.PerformedMoveMessage;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CloudGUIComponent {

    private final GameController gameController;

    private List<ColoredPawnImageView> students;
    private final int cloudIndex;
    private boolean empty;

    private final Group cloudGroup;
    private final GridPane cloudGrid;
    private final Button button;

    private final EventHandler<MouseEvent> buttonClicked;
    private MoveRequestMessage requestMessage;

    public CloudGUIComponent(Group cloud, int cloudIndex, GameController gameController) {

        this.gameController = gameController;
        this.cloudIndex = cloudIndex;
        empty = true;

        ((ImageView) cloud.getChildren().get(0)).setImage(ImageFactory.cloudImage);

        cloudGroup = cloud;
        cloudGrid = (GridPane) cloud.getChildren().get(1);
        button = (Button) cloud.getChildren().get(2);

        initializeStudentImageViews();

        buttonClicked = mouseEvent -> {

            try {
                manageInput(mouseEvent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }

    private void initializeStudentImageViews() {

        students = new ArrayList<>(3);

        ColoredPawnImageView firstStudentImage = new ColoredPawnImageView(ImageFactory.STUDENT_SIZE);
        ColoredPawnImageView secondStudentImage = new ColoredPawnImageView(ImageFactory.STUDENT_SIZE);
        ColoredPawnImageView thirdStudentImage = new ColoredPawnImageView(ImageFactory.STUDENT_SIZE);

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
                ColoredPawnImageView fourthStudentImage = new ColoredPawnImageView(ImageFactory.STUDENT_SIZE);
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
        button.addEventHandler(MouseEvent.MOUSE_CLICKED, buttonClicked);
        cloudGroup.setEffect(gameController.getBorderGlowEffect());
    }

    public void stopListeningToInput() {

        button.removeEventHandler(MouseEvent.MOUSE_CLICKED, buttonClicked);
        cloudGroup.setEffect(null);
    }

    public void manageInput(MouseEvent mouseEvent) throws IOException {

        if(mouseEvent.getButton() == MouseButton.PRIMARY) {

            EriantysClient.getInstance().sendToServer(new PerformedMoveMessage(requestMessage, new ChooseCloud(cloudIndex)));
            stopListeningToInput();
        }
    }

    public void setStudents(PawnColor color, int numberOfStudents) {

        for(int i = 0; i < numberOfStudents; i++) {

            for(ColoredPawnImageView image : students) {

                if(image.getColor() == null) {

                    image.setStudentOfColor(color);
                    empty = false;
                    break;
                }
            }
        }
    }

    public void clearStudents() {

        for(ColoredPawnImageView image : students) image.clearColor();
        empty = true;
    }

    public boolean isEmpty() {

        return empty;
    }
}
