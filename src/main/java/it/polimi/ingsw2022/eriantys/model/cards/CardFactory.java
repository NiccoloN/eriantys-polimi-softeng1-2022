package it.polimi.ingsw2022.eriantys.model.cards;

import com.google.gson.Gson;
import it.polimi.ingsw2022.eriantys.model.players.Mage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * This class is used to easily build Card instances, reading data from files
 * @author Niccolò Nicolosi
 * @see Card
 * @see HelperCard
 * @see CharacterCard
 */
public class CardFactory {

    private static final Gson gson = new Gson();
    private static final String RESOURCES_DIR = "resources";
    private static final String HELPERS_FILE_NAME = "helpers.json";
    private static final String CHARACTERS_FILE_NAME = "characters.json";
    private static HelperCard[] helperCardSamples;
    private static CharacterCard[] characterCardSamples;

    /**
     * Reads the json file containing helpers' data and stores card samples
     * @throws IOException if the specified path is not valid or the file could not be opened
     */
    private static void readHelpersFile() throws IOException {

        Path path = Path.of(RESOURCES_DIR, HELPERS_FILE_NAME);
        String json = Files.readString(path);
        helperCardSamples = gson.fromJson(json, HelperCard[].class);
    }

    /**
     * Reads the json file containing characters' data and stores card samples
     * @throws IOException if the specified path is not valid or the file could not be opened
     */
    private static void readCharacterFile() throws IOException {

        Path path = Path.of(RESOURCES_DIR, CHARACTERS_FILE_NAME);
        String json = Files.readString(path);
        characterCardSamples = gson.fromJson(json, CharacterCard[].class);
    }

    /**
     * @param index the index of the requested helper card
     * @param mage the mage associated with the requested helper card
     * @return a new instance of the requested helper card, specified by the given index and mage
     * @throws RuntimeException if the given index is invalid
     * @throws IOException if the specified path is not valid or the file could not be opened
     */
    public static HelperCard createHelperCard(int index, Mage mage) throws IOException {

        if (helperCardSamples == null) readHelpersFile();
        if (index < 1 || index > helperCardSamples.length)
            throw new RuntimeException("Invalid index: helper cars' indices go from 1 to " + helperCardSamples.length);

        HelperCard sample = helperCardSamples[index - 1];
        return new HelperCard(sample.index, sample.priority, sample.movement, mage);
    }

    /**
     * @param index the index of the requested character card
     * @return a new instance of the requested character card, specified by the given index
     * @throws RuntimeException if the given index is invalid
     * @throws IOException if the specified path is not valid or the file could not be opened
     */
    public static CharacterCard createCharacterCard(int index) throws IOException {

        if (characterCardSamples == null) readCharacterFile();
        if (index < 1 || index > characterCardSamples.length)
            throw new RuntimeException("Invalid index: helper cars' indices go from 1 to " + characterCardSamples.length);

        CharacterCard sample = characterCardSamples[index - 1];
        return new CharacterCard(sample.index, null); //TODO skill factory
    }
}
