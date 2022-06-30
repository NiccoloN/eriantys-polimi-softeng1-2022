package it.polimi.ingsw2022.eriantys.server.model.cards;

import com.google.gson.Gson;
import it.polimi.ingsw2022.eriantys.server.model.players.Mage;

import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

/**
 * This class is used to easily build Card instances, reading data from files
 *
 * @author Niccolò Nicolosi
 * @see Card
 * @see HelperCard
 * @see CharacterCard
 */
public class CardFactory {
    
    private static final Gson gson = new Gson();
    private static final String HELPERS_FILE_NAME = "helpers.json";
    private static final String CHARACTERS_FILE_NAME = "characters.json";
    private static HelperCard[] helperCardSamples;
    private static CharacterCard[] characterCardSamples;
    
    /**
     * @param index the index of the requested helper card
     * @param mage  the mage associated with the requested helper card
     * @return a new instance of the requested helper card, specified by the given index and mage
     * @throws RuntimeException if the given index is invalid
     * @throws IOException      if the specified path is not valid or the file could not be opened
     */
    public static HelperCard createHelperCard(int index, Mage mage) throws IOException {
        
        if (helperCardSamples == null) readHelpersFile();
        if (index < 1 || index > helperCardSamples.length)
            throw new RuntimeException("Invalid index: helper cards' indices go from 1 to " + helperCardSamples.length);
        
        HelperCard sample = helperCardSamples[index - 1];
        return new HelperCard(sample.index, sample.priority, sample.movement, mage);
    }
    
    /**
     * @param index the index of the requested character card
     * @return a new instance of the requested character card, specified by the given index
     * @throws RuntimeException if the given index is invalid
     * @throws IOException      if the specified path is not valid or the file could not be opened
     */
    public static CharacterCard createCharacterCard(int index) throws IOException {
        
        if (characterCardSamples == null) readCharacterFile();
        if (index < 1 || index > characterCardSamples.length)
            throw new RuntimeException("Invalid index: helper cards' indices go from 1 to " + characterCardSamples.length);
        
        CharacterCard sample = characterCardSamples[index - 1];
        return new CharacterCard(sample.index, sample.effect, sample.getCost());
    }
    
    /**
     * Reads the json file containing helpers' data and stores card samples
     *
     * @throws IOException if the specified path is not valid or the file could not be opened
     */
    private static void readHelpersFile() throws IOException {
        
        Scanner scanner = new Scanner(Objects.requireNonNull(CardFactory.class.getClassLoader().getResource(HELPERS_FILE_NAME)).openStream());
        String json = scanner.useDelimiter("\\Z").next();
        helperCardSamples = gson.fromJson(json, HelperCard[].class);
    }
    
    /**
     * Reads the json file containing characters' data and stores card samples
     *
     * @throws IOException if the specified path is not valid or the file could not be opened
     */
    private static void readCharacterFile() throws IOException {
        
        Scanner scanner = new Scanner(Objects.requireNonNull(CardFactory.class.getClassLoader().getResource(CHARACTERS_FILE_NAME)).openStream());
        String json = scanner.useDelimiter("\\Z").next();
        characterCardSamples = gson.fromJson(json, CharacterCard[].class);
    }
}
