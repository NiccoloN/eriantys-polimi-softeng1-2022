package it.polimi.ingsw2022.eriantys.client.view.cli;

import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

/**
 * This class provides access to various asciiArts in the form of strings
 */
public class AsciiArts {
    
    private static String titleString, cloud1String, cloud2String, cloud3String, cloud4String;
    
    public static void loadFromFiles() throws IOException {
        
        titleString = loadString("ascii arts/title.txt");
        cloud1String = loadString("ascii arts/cloud1.txt");
        cloud2String = loadString("ascii arts/cloud2.txt");
        cloud3String = loadString("ascii arts/cloud3.txt");
        cloud4String = loadString("ascii arts/cloud4.txt");
    }
    
    public static String getTitleString() {
        
        return titleString;
    }
    
    public static String getCloud1String() {
        
        return cloud1String;
    }
    
    public static String getCloud2String() {
        
        return cloud2String;
    }
    
    public static String getCloud3String() {
        
        return cloud3String;
    }
    
    public static String getCloud4String() {
        
        return cloud4String;
    }
    
    private static String loadString(String resourcePath) throws IOException {
        
        Scanner scanner = new Scanner(Objects.requireNonNull(AsciiArts.class.getClassLoader().getResource(resourcePath)).openStream());
        scanner.useDelimiter("\\Z");
        String string = scanner.next();
        scanner.close();
        
        return string;
    }
}
