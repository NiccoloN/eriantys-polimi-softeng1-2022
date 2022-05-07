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

public class EnterUsername extends MenuSceneState {

    private String username;
    private final Message requestMessage;

    public EnterUsername(EriantysCLI cli, MenuScene scene, Message requestMessage) {

        super(cli, scene);

        this.requestMessage = requestMessage;
        username = "";
    }

    @Override
    public void enter() {

        getScene().getEnterUsernamePrompt().setHidden(false);
        getScene().getUsernameTextArea().setHidden(false);
    }

    @Override
    public void exit() {

        getScene().getEnterUsernamePrompt().setHidden(true);
        getScene().getUsernameTextArea().setHidden(true);
    }

    @Override
    public void manageInput(Input input) throws IOException {

        if(input.triggersAction(Action.SELECT)) {

            EriantysClient client = EriantysClient.getInstance();
            client.sendToServer(new UsernameChoiceMessage(requestMessage, username));

            //TODO client.sendToServer(new AbortMessage(requestMessage));
        }

        Optional<Character> character = input.getChar();
        if(character.isPresent()) {

            char c = character.get();

            TextAreaCLIComponent textArea = getScene().getUsernameTextArea();

            if(c == 8 || c == 127) {

                String prevText = textArea.getText();
                textArea.setText(prevText.substring(0, Math.max(0, prevText.length() - 1)));
            }
            else if (c >= 32 && c <= 126 && username.length() <= 10) textArea.appendText(String.valueOf(c));

            username = textArea.getText();
        }
    }
}
