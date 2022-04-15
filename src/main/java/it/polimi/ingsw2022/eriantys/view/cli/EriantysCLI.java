package it.polimi.ingsw2022.eriantys.view.cli;

import it.polimi.ingsw2022.eriantys.view.cli.components.CLIComponent;
import it.polimi.ingsw2022.eriantys.view.cli.components.CloudCLIComponent;
import it.polimi.ingsw2022.eriantys.view.cli.components.IslandCLIComponent;
import it.polimi.ingsw2022.eriantys.view.cli.components.player.PlayerStatusCLIComponent;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.AttributedString;
import org.jline.utils.InfoCmp;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw2022.eriantys.view.cli.components.AnsiColorCodes.*;

public class EriantysCLI {

    private static final int TERMINAL_WIDTH = 180, TERMINAL_HEIGHT = 50;
    
    private static final String TEAM_WHITE_COLOR = WHITE_UNDERLINED_BRIGHT;
    private static final String TEAM_BLACK_COLOR = BLACK_UNDERLINED_BRIGHT;
    private static final String TEAM_GRAY_COLOR = WHITE_UNDERLINED;

    private final Terminal terminal;
    private final LineReader lineReader;
    private final AttributedString prompt;

    private final CLIComponent title;
    private final IslandCLIComponent[] islands;
    private final List<CloudCLIComponent> clouds;
    private final List<PlayerStatusCLIComponent> players;

    public EriantysCLI() throws IOException {

        terminal = TerminalBuilder
                .builder()
                .build();

        lineReader = LineReaderBuilder
                .builder()
                .terminal(terminal)
                .build();

        prompt = new AttributedString(RED + ">" + RESET);

        title = new CLIComponent(73, (" ________            __                       __                         \n" + "|        \\          |  \\                     |  \\                        \n" + "| $$$$$$$$  ______   \\$$  ______   _______  _| $$_    __    __   _______ \n" + "| $$__     /      \\ |  \\ |      \\ |       \\|   $$ \\  |  \\  |  \\ /       \\\n" + "| $$  \\   |  $$$$$$\\| $$  \\$$$$$$\\| $$$$$$$\\\\$$$$$$  | $$  | $$|  $$$$$$$\n" + "| $$$$$   | $$   \\$$| $$ /      $$| $$  | $$ | $$ __ | $$  | $$ \\$$    \\ \n" + "| $$_____ | $$      | $$|  $$$$$$$| $$  | $$ | $$|  \\| $$__/ $$ _\\$$$$$$\\\n" + "| $$     \\| $$      | $$ \\$$    $$| $$  | $$  \\$$  $$ \\$$    $$|       $$\n" + " \\$$$$$$$$ \\$$       \\$$  \\$$$$$$$ \\$$   \\$$   \\$$$$  _\\$$$$$$$ \\$$$$$$$ \n" + "                                                     |  \\__| $$          \n" + "                                                      \\$$    $$          \n" + "                                                       \\$$$$$$           \n")
                .split("\n"));
        title.setColor(YELLOW);

        islands = new IslandCLIComponent[12];
        for(int n = 0; n < islands.length; n++) islands[n] = new IslandCLIComponent(n + 1);

        clouds = new ArrayList<>(4);
        for(int n = 0; n < 4; n++) clouds.add(new CloudCLIComponent(n + 1));

        players = new ArrayList<>(4);
        players.add(new PlayerStatusCLIComponent(1, "player_1", TEAM_WHITE_COLOR));
        players.add(new PlayerStatusCLIComponent(2, "player_2", TEAM_WHITE_COLOR));
        players.add(new PlayerStatusCLIComponent(3, "player_3", TEAM_BLACK_COLOR));
        players.add(new PlayerStatusCLIComponent(4, "player_4", TEAM_GRAY_COLOR));

        setComponentsPositions();
    }

    private void setComponentsPositions() {

        title.setPosition(TERMINAL_WIDTH / 2 - title.getWidth() / 2, 0);

        int offsetY = title.getHeight() + 2;
        islands[0].setPosition(1 + TERMINAL_WIDTH / 2, offsetY);
        islands[1].setPosition(1 + TERMINAL_WIDTH / 2 + islands[0].getWidth(), offsetY+ islands[0].getHeight() / 2);
        islands[2].setPosition(1 + TERMINAL_WIDTH / 2 + islands[0].getWidth() * 2, offsetY + islands[0].getHeight());
        islands[3].setPosition(1 + TERMINAL_WIDTH / 2 + islands[0].getWidth() * 2, offsetY + islands[0].getHeight() * 2);
        islands[4].setPosition(1 + TERMINAL_WIDTH / 2 + islands[0].getWidth(), offsetY + islands[0].getHeight() * 2 + islands[0].getHeight() / 2);
        islands[5].setPosition(1 + TERMINAL_WIDTH / 2, offsetY + islands[0].getHeight() * 3);
        islands[6].setPosition(-1 + TERMINAL_WIDTH / 2 - islands[0].getWidth(), offsetY + islands[0].getHeight() * 3);
        islands[7].setPosition(-1 + TERMINAL_WIDTH / 2 - islands[0].getWidth() * 2, offsetY + islands[0].getHeight() * 2 + islands[0].getHeight() / 2);
        islands[8].setPosition(-1 + TERMINAL_WIDTH / 2 - islands[0].getWidth() * 3, offsetY + islands[0].getHeight() * 2);
        islands[9].setPosition(-1 + TERMINAL_WIDTH / 2 - islands[0].getWidth() * 3, offsetY + islands[0].getHeight());
        islands[10].setPosition(-1 + TERMINAL_WIDTH / 2 - islands[0].getWidth() * 2, offsetY + islands[0].getHeight() / 2);
        islands[11].setPosition(-1 + TERMINAL_WIDTH / 2 - islands[0].getWidth(), offsetY);

        islands[0].setTeamColor(TEAM_WHITE_COLOR);
        islands[0].setTower(true);
        islands[0].setMother(true);

        for(int n = 0; n < clouds.size(); n++) clouds.get(n).setPosition(
                TERMINAL_WIDTH / 2 - clouds.get(0).getWidth() * clouds.size() / 2 - ((clouds.size() % 2 == 0 && n < clouds.size() / 2) ? 2 : 1) + (clouds.get(0).getWidth() + 1) * n,
                islands[2].getY() + islands[2].getHeight() - clouds.get(0).getHeight() / 2 - 1);

        players.get(0).setPosition(0, 0);
        players.get(1).setPosition(0, players.get(0).getHeight());
        players.get(2).setPosition(TERMINAL_WIDTH - players.get(0).getWidth(), 0);
        players.get(3).setPosition(TERMINAL_WIDTH - players.get(0).getWidth(), players.get(0).getHeight());
    }

    public void start() {

        while (true) {

            terminal.writer().print("\u001B[8;" + TERMINAL_HEIGHT + ";" + TERMINAL_WIDTH + "t");
            terminal.flush();

            if (terminal.getWidth() == TERMINAL_WIDTH && terminal.getHeight() == TERMINAL_HEIGHT) {

                clear();

                printComponent(title);
                lineDown(1);
                printLine(TERMINAL_WIDTH);
                for(IslandCLIComponent island : islands) printComponent(island);
                for(CloudCLIComponent cloud : clouds) printComponent(cloud);
                for(PlayerStatusCLIComponent player : players) printComponent(player);
                terminal.flush();

                String line = lineReader.readLine(prompt.toAnsi());
            }
        }
    }

    private void printComponent(CLIComponent component) {

        int rowNum = 0;
        for (String row : component.getRows()) {

            setCursorPos(component.getX(), component.getY() + rowNum);
            terminal.writer().print(row);
            rowNum++;
        }
    }

    private void printLine(int length) {

        if (length < 0) {

            clear();
            throw new InvalidParameterException("Length must be >= 0");
        }
        terminal.writer().print("_".repeat(length));
    }
    
    private void clear() {

        terminal.writer().print("\u001Bc");
        terminal.flush();
    }

    private void setCursorPos(int x, int y) {

        if (x < 0) {

            clear();
            throw new InvalidParameterException("Cursor x must be >= 0");
        }
        if (y < 0) {

            clear();
            throw new InvalidParameterException("Cursor y must be >= 0");
        }
        terminal.writer().print("\u001B[" + (y + 1) + ";" + (x + 1) + "f");
    }

    private void lineUp(int lines) {

        if (lines < 0) {

            clear();
            throw new InvalidParameterException("Lines must be >= 0");
        }
        for (int n = 0; n < lines; n++) terminal.puts(InfoCmp.Capability.cursor_up);
    }

    private void lineDown(int lines) {

        if (lines < 0) {

            clear();
            throw new InvalidParameterException("Lines must be >= 0");
        }
        for (int n = 0; n < lines; n++) terminal.puts(InfoCmp.Capability.cursor_down);
    }

    private void charRight(int chars) {

        if (chars < 0) {

            clear();
            throw new InvalidParameterException("Chars must be >= 0");
        }
        for (int n = 0; n < chars; n++) terminal.puts(InfoCmp.Capability.cursor_right);
    }

    private void charLeft(int chars) {

        if (chars < 0) {

            clear();
            throw new InvalidParameterException("Chars must be >= 0");
        }
        for (int n = 0; n < chars; n++) terminal.puts(InfoCmp.Capability.cursor_left);
    }
}
