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
    private static final String helpersFilePath = "resources/helpers.json";
    private static HelperCard[] helperCardSamples;

    /**
     * Reads the json file containing helpers' data and stores card samples
     * @throws IOException if helperFilePath is not a valid path or the file could not be opened
     */
    private static void readHelpersFile() throws IOException {

        Path path = Path.of(helpersFilePath);
        String json = Files.readString(path);
        helperCardSamples = gson.fromJson(json, HelperCard[].class);
    }

    /**
     * @param index the index of the requested helper card
     * @param mage the mage associated with the requested helper card
     * @return a new instance of the requested helper card, specified by the given index and mage.
     * @throws IOException if
     */
    public static HelperCard createHelperCard(int index, Mage mage) throws IOException {

        if (helperCardSamples == null) readHelpersFile();

        HelperCard sample = helperCardSamples[index - 1];
        return new HelperCard(sample.index, sample.priority, sample.movement, mage);
    }
}
