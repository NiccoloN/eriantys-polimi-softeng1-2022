package it.polimi.ingsw2022.eriantys.client.view;

import java.util.concurrent.TimeoutException;

public interface View {

    void start() throws TimeoutException;

    String getUsername();
}
