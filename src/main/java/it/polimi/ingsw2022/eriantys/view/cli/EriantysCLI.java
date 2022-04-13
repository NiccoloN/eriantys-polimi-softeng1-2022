package it.polimi.ingsw2022.eriantys.view.cli;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.AttributedString;
import org.jline.utils.InfoCmp;

import java.io.IOException;

import static it.polimi.ingsw2022.eriantys.view.cli.AnsiColorCodes.*;

public class EriantysCLI {

    private static final String TEAM_WHITE_COLOR = WHITE_UNDERLINED_BRIGHT;
    private static final String TEAM_BLACK_COLOR = BLACK_UNDERLINED_BRIGHT;
    private static final String TEAM_GREY_COLOR = WHITE_UNDERLINED;

    private final Terminal terminal;
    private final LineReader lineReader;
    private final AttributedString prompt;

    private int currCursorX, currCursorY;

    private final CLIComponent title;
    private final IslandCLIComponent[] islands;

    public EriantysCLI() throws IOException {

        terminal = TerminalBuilder
                .builder()
                .build();

        lineReader = LineReaderBuilder
                .builder()
                .terminal(terminal)
                .build();

        prompt = new AttributedString(RED + ">" + RESET);

        currCursorX = 0;
        currCursorY = 0;

        title = new CLIComponent(73, (" ________            __                       __                         \n" + "|        \\          |  \\                     |  \\                        \n" + "| $$$$$$$$  ______   \\$$  ______   _______  _| $$_    __    __   _______ \n" + "| $$__     /      \\ |  \\ |      \\ |       \\|   $$ \\  |  \\  |  \\ /       \\\n" + "| $$  \\   |  $$$$$$\\| $$  \\$$$$$$\\| $$$$$$$\\\\$$$$$$  | $$  | $$|  $$$$$$$\n" + "| $$$$$   | $$   \\$$| $$ /      $$| $$  | $$ | $$ __ | $$  | $$ \\$$    \\ \n" + "| $$_____ | $$      | $$|  $$$$$$$| $$  | $$ | $$|  \\| $$__/ $$ _\\$$$$$$\\\n" + "| $$     \\| $$      | $$ \\$$    $$| $$  | $$  \\$$  $$ \\$$    $$|       $$\n" + " \\$$$$$$$$ \\$$       \\$$  \\$$$$$$$ \\$$   \\$$   \\$$$$  _\\$$$$$$$ \\$$$$$$$ \n" + "                                                     |  \\__| $$          \n" + "                                                      \\$$    $$          \n" + "                                                       \\$$$$$$           \n")
                .split("\n"));
        title.setColor(YELLOW);

        islands = new IslandCLIComponent[12];
        for(int n = 0; n < islands.length; n++) islands[n] = new IslandCLIComponent(n + 1, TEAM_WHITE_COLOR);
    }

    public void start() {

        while (true) {

            setComponentsPositions();

            clear();
            printComponent(title);
            printSeparator();
            for(IslandCLIComponent island : islands) printComponent(island);
            terminal.flush();

            String line = lineReader.readLine(prompt.toAnsi());
        }
    }

    private void setComponentsPositions() {

        title.setPosition(terminal.getWidth() / 2 - title.getWidth() / 2, 0);

        int offsetY = title.getHeight() + 2;
        islands[0].setPosition(1 + terminal.getWidth() / 2, offsetY);
        islands[1].setPosition(1 + terminal.getWidth() / 2 + islands[0].getWidth(), offsetY+ islands[0].getHeight() / 2);
        islands[2].setPosition(1 + terminal.getWidth() / 2 + islands[0].getWidth() * 2, offsetY + islands[0].getHeight());
        islands[3].setPosition(1 + terminal.getWidth() / 2 + islands[0].getWidth() * 2, offsetY + islands[0].getHeight() * 2);
        islands[4].setPosition(1 + terminal.getWidth() / 2 + islands[0].getWidth(), offsetY + islands[0].getHeight() * 2 + islands[0].getHeight() / 2);
        islands[5].setPosition(1 + terminal.getWidth() / 2, offsetY + islands[0].getHeight() * 3);
        islands[6].setPosition(-1 + terminal.getWidth() / 2 - islands[0].getWidth(), offsetY + islands[0].getHeight() * 3);
        islands[7].setPosition(-1 + terminal.getWidth() / 2 - islands[0].getWidth() * 2, offsetY + islands[0].getHeight() * 2 + islands[0].getHeight() / 2);
        islands[8].setPosition(-1 + terminal.getWidth() / 2 - islands[0].getWidth() * 3, offsetY + islands[0].getHeight() * 2);
        islands[9].setPosition(-1 + terminal.getWidth() / 2 - islands[0].getWidth() * 3, offsetY + islands[0].getHeight());
        islands[10].setPosition(-1 + terminal.getWidth() / 2 - islands[0].getWidth() * 2, offsetY + islands[0].getHeight() / 2);
        islands[11].setPosition(-1 + terminal.getWidth() / 2 - islands[0].getWidth(), offsetY);

        islands[0].setTower(true);
        islands[0].setMother(true);
        islands[0].setTeamColor(TEAM_BLACK_COLOR);
    }

    private void printComponent(CLIComponent component) {

        int rowNum = 0;
        for (String row : component.getRows()) {

            setCursorPos(component.getX(), component.getY() + rowNum);
            terminal.writer().print(row);
            currCursorX = component.getX() + component.getWidth();
            rowNum++;
        }
        setCursorPos(component.getX() + component.getWidth(), component.getY());
    }
    
    private void printSeparator() {
        
        setCursorPos(0, title.getHeight());
        terminal.writer().print("_".repeat(terminal.getWidth()));
    }
    
    private void clear() {

        terminal.puts(InfoCmp.Capability.clear_screen);
        currCursorX = 0;
        currCursorY = 0;
    }

    private void setCursorPos(int x, int y) {

        setCursorY(y);
        setCursorX(x);
    }

    public void setCursorX(int x) {

        int steps = x - currCursorX;
        if (steps == 0) return;
        if (steps > 0) charRight(steps);
        else charLeft(-steps);
    }

    public void setCursorY(int y) {

        int steps = y - currCursorY;
        if (steps == 0) return;
        if (steps > 0) lineDown(steps);
        else lineUp(-steps);
    }

    private void lineUp(int lines) {

        assert lines >= 1;
        for (int n = 0; n < lines; n++) terminal.puts(InfoCmp.Capability.cursor_up);
        currCursorY -= lines;
    }

    private void lineDown(int lines) {

        assert lines >= 1;
        for (int n = 0; n < lines; n++) terminal.puts(InfoCmp.Capability.cursor_down);
        currCursorY += lines;
        currCursorX = 0;
    }

    private void charRight(int chars) {

        assert chars >= 1;
        for (int n = 0; n < chars; n++) terminal.puts(InfoCmp.Capability.cursor_right);
        currCursorX += chars;
    }

    private void charLeft(int chars) {

        assert chars >= 1;
        for (int n = 0; n < chars; n++) terminal.puts(InfoCmp.Capability.cursor_left);
        currCursorX -= chars;
    }
}
