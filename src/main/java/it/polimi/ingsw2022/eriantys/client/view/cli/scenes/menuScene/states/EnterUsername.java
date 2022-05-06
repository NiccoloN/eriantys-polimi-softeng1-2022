package it.polimi.ingsw2022.eriantys.client.view.cli.scenes.menuScene.states;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.client.view.cli.Action;
import it.polimi.ingsw2022.eriantys.client.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.client.view.cli.Input;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.components.TextAreaCLIComponent;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.menuScene.MenuScene;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.states.CLISceneState;
import it.polimi.ingsw2022.eriantys.messages.Message;
import it.polimi.ingsw2022.eriantys.messages.toServer.AbortMessage;
import it.polimi.ingsw2022.eriantys.messages.toServer.UsernameChoiceMessage;

import java.io.IOException;
import java.util.Optional;

public class EnterUsername extends CLISceneState {

    private String username;
    private Message requestMessage;

    public EnterUsername(EriantysCLI cli, MenuScene scene, Message requestMessage) {

        super(cli, scene);
        this.requestMessage = requestMessage;
    }

    @Override
    public void enter() {

        getScene().getUsernameTextArea().setHidden(false);
    }

    @Override
    public void exit() {

        getScene().getUsernameTextArea().setHidden(true);
    }

    @Override
    public void manageInput(Input input) throws IOException {

        if(input.triggersAction(Action.SELECT)) {

            EriantysClient client = EriantysClient.getInstance();
            if(username != null && username.length() > 0 && username.length() < 10)
                client.sendToServer(new UsernameChoiceMessage(requestMessage, username));
            else client.sendToServer(new AbortMessage(requestMessage));
        }

        Optional<Character> character = input.getChar();
        if(character.isPresent()) {

            TextAreaCLIComponent textArea = getScene().getUsernameTextArea();

            String prevText = textArea.getText();
            if(prevText == null) prevText = "";

            if(character.get() == 8) textArea.setText(prevText.substring(0, Math.max(0, prevText.length() - 1)));
            else textArea.setText(prevText + character.get());

            username = textArea.getText();
        }
    }

    @Override
    public MenuScene getScene() {

        return (MenuScene) super.getScene();
    }
}
