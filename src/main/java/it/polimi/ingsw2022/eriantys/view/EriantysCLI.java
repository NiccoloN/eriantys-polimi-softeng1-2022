package it.polimi.ingsw2022.eriantys.view;

import org.jline.builtins.Completers;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.util.Scanner;

public class EriantysCLI {

    Terminal terminal;
    LineReader lineReader;

    public EriantysCLI() throws IOException {

        TerminalBuilder terminalBuilder = TerminalBuilder.builder();
        terminal = terminalBuilder.build();

        terminal.writer().println("Hello world!");
        terminal.
    }

    public void start() {
    }
}
