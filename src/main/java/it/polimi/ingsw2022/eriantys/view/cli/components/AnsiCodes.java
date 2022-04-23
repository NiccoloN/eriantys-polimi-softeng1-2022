package it.polimi.ingsw2022.eriantys.view.cli.components;

/**
 * This class provides different ansi codes and ansi-related utility methods
 * @author Niccolò Nicolosi
 */
public class AnsiCodes {

    public static final char ESCAPE_CHAR = '\u001B';

    //Reset
    public static final String RESET ="\u001B[0;40;97m";

    public static final String BOLD = "\u001B[1m";
    public static final String UNDERLINED = "\u001B[4m";
    public static final String BLINKING = "\u001B[6m";

    // Regular colors
    public static final String BLACK = "\u001B[30m";   
    public static final String RED = "\u001B[31m";     
    public static final String GREEN = "\u001B[32m";   
    public static final String YELLOW = "\u001B[33m";  
    public static final String BLUE = "\u001B[34m";    
    public static final String PURPLE = "\u001B[35m";  
    public static final String CYAN = "\u001B[36m";    
    public static final String WHITE = "\u001B[37m";

    //High intensity colors
    public static final String BLACK_BRIGHT = "\u001B[90m";
    public static final String RED_BRIGHT = "\u001B[91m";
    public static final String GREEN_BRIGHT = "\u001B[92m";
    public static final String YELLOW_BRIGHT = "\u001B[93m";
    public static final String BLUE_BRIGHT = "\u001B[94m";
    public static final String PURPLE_BRIGHT = "\u001B[95m";
    public static final String CYAN_BRIGHT = "\u001B[96m";
    public static final String WHITE_BRIGHT = "\u001B[97m";

    //Background colors
    public static final String BLACK_BACKGROUND = "\u001B[40m";  
    public static final String RED_BACKGROUND = "\u001B[41m";    
    public static final String GREEN_BACKGROUND = "\u001B[42m";  
    public static final String YELLOW_BACKGROUND = "\u001B[43m"; 
    public static final String BLUE_BACKGROUND = "\u001B[44m";   
    public static final String PURPLE_BACKGROUND = "\u001B[45m"; 
    public static final String CYAN_BACKGROUND = "\u001B[46m";   
    public static final String WHITE_BACKGROUND = "\u001B[47m";

    //High intensity backgrounds
    public static final String BLACK_BACKGROUND_BRIGHT = "\u001B[100m";
    public static final String RED_BACKGROUND_BRIGHT = "\u001B[101m";
    public static final String GREEN_BACKGROUND_BRIGHT = "\u001B[102m";
    public static final String YELLOW_BACKGROUND_BRIGHT = "\u001B[103m";
    public static final String BLUE_BACKGROUND_BRIGHT = "\u001B[104m";
    public static final String PURPLE_BACKGROUND_BRIGHT = "\u001B[105m";
    public static final String CYAN_BACKGROUND_BRIGHT = "\u001B[106m";
    public static final String WHITE_BACKGROUND_BRIGHT = "\u001B[107m";

    /**
     * @param ansiString a string containing ansi codes
     * @return a string identical to the given one, but stripped of any ansi code
     */
    public static String noAnsiString(String ansiString) {

        char[] chars = ansiString.toCharArray();

        StringBuilder stringBuilder = new StringBuilder();
        boolean toConsider = true;

        for (int n = 0; n < chars.length; n++) {

            if (chars[n] == 27) toConsider = false;
            if (toConsider) stringBuilder.append(chars[n]);
            if (chars[n] == 'm') toConsider = true;
        }

        return stringBuilder.toString();
    }

    /**
     * @param ansiString a string containing ansi codes
     * @param textStartIndex the index of the character to consider as the start of the substring (without considering ansi codes)
     * @param textEndIndex the index of the character to consider as the end of the substring (without considering ansi codes)
     * @return a substring (including ansi codes from the start index to right after the end index) of the given string
     */
    public static String ansiTextSubstring(String ansiString, int textStartIndex, int textEndIndex) {

        char[] chars = ansiString.toCharArray();
        int textLength = textEndIndex - textStartIndex;

        StringBuilder stringBuilder = new StringBuilder();
        int length = 0;
        boolean toConsider = true;
        boolean finished = false;

        if (textLength > 0) {

            for (int n = 0; n < chars.length && !finished; n++) {

                stringBuilder.append(chars[n]);

                if (chars[n] == 27) toConsider = false;
                if (toConsider) length++;
                if (chars[n] == 'm') toConsider = true;

                if (length == textLength) finished = true;
                if (n + 1 < chars.length && (!toConsider || chars[n + 1] == 27)) finished = false;
            }
        }

        return stringBuilder.toString();
    }
}
