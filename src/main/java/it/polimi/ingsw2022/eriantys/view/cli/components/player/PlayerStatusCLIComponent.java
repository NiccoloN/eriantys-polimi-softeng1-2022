package it.polimi.ingsw2022.eriantys.view.cli.components.player;

import it.polimi.ingsw2022.eriantys.view.cli.components.CLIComponent;

import java.security.InvalidParameterException;

import static it.polimi.ingsw2022.eriantys.view.cli.components.AnsiColorCodes.RESET;

public class PlayerStatusCLIComponent extends CLIComponent {

    static final String PLAYER_STATUS_DEFAULT_COLOR = RESET;

    private final int index;
    private final SchoolTablesCLIComponent tablesCLIComponent;
    private final PlayerStatsCLIComponent statsCLIComponent;

    public PlayerStatusCLIComponent(int index, String nickname, String teamAnsiColor) {

        super(41, 9);

        if (index < 0 || index > 9) throw new InvalidParameterException("Index must be a positive single digit integer");
        this.index = index;

        tablesCLIComponent = new SchoolTablesCLIComponent(nickname);
        statsCLIComponent = new PlayerStatsCLIComponent(teamAnsiColor);

        buildRows();
    }

    private void buildRows() {

        String[] tablesRows = tablesCLIComponent.getRows();
        String[] statsRows = statsCLIComponent.getRows();
        for(int n = 0; n < getHeight(); n++) setRow(n, tablesRows[n] + statsRows[n]);
    }

    @Override
    public String[] getRows() {

        buildRows();
        return super.getRows();
    }
}
