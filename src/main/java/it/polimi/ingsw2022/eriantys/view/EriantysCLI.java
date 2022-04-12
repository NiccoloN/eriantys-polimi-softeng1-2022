package it.polimi.ingsw2022.eriantys.view;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.MaskingCallback;
import org.jline.reader.impl.LineReaderImpl;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.AttributedString;
import org.jline.utils.InfoCmp;

import java.io.IOException;

public class EriantysCLI {

    private final Terminal terminal;
    private final LineReader lineReader;
    private AttributedString prompt;

    public EriantysCLI() throws IOException {

        terminal = TerminalBuilder
                .builder()
                .dumb(true)
                .build();

        lineReader = LineReaderBuilder
                .builder()
                .terminal(terminal)
                .build();

        prompt = new AttributedString("\u001B[33m>\u001B[0m");
    }

    public void start() {

        while (true) {

            terminal.puts(InfoCmp.Capability.clear_screen);
            terminal.flush();

            String line = lineReader.readLine(prompt.toAnsi());

            terminal.writer().println(AttributedString
                    .fromAnsi("\u001B[33m" + line + "\u001B[0m" + " ciao")
                    .toAnsi(terminal));

            terminal.flush();
        }
    }
}
