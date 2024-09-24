package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
// import java.util.Iterator;
// import java.util.Map;

import org.json.JSONArray;

/**
 * An implementation of the Translator interface which reads in the translation
 * data from a JSON file. The data is read in once each time an instance of this class is constructed.
 */
public class JSONTranslator implements Translator {

    private static final String ALPHA3 = "alpha3";
    private static final String ALPHA2 = "alpha2";
    private final JSONArray jsonArray;

    /**
     * Constructs a JSONTranslator using data from the sample.json resources file.
     */
    public JSONTranslator() {
        this("sample.json");
    }

    /**
     * Constructs a JSONTranslator populated using data from the specified resources file.
     * @param filename the name of the file in resources to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public JSONTranslator(String filename) {
        // read the file to get the data to populate things...
        try {

            String jsonString = Files.readString(Paths.get(getClass().getClassLoader().getResource(filename).toURI()));

            this.jsonArray = new JSONArray(jsonString);

        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<String> getCountryLanguages(String country) {
        for (int i = 0; i < jsonArray.length(); i++) {
            if (jsonArray.getJSONObject(i).getString(ALPHA3).equals(country) || jsonArray.getJSONObject(i)
                    .getString(ALPHA2).equals(country)) {
                ArrayList<String> listLanguages = new ArrayList<>(jsonArray.getJSONObject(i).keySet());
                listLanguages.remove("id");
                listLanguages.remove("alpha2");
                listLanguages.remove("alpha3");
                return listLanguages;
            }
        }
        return null;
    }

    @Override
    public List<String> getCountries() {
        ArrayList<String> listCountries = new ArrayList<>(jsonArray.length());

        for (int i = 0; i < jsonArray.length(); i++) {
            listCountries.add(jsonArray.getJSONObject(i).getString(ALPHA3));
        }
        return listCountries;
    }

    @Override
    public String translate(String country, String language) {
        for (int i = 0; i < jsonArray.length(); i++) {
            if (jsonArray.getJSONObject(i).getString(ALPHA3).equals(country) || jsonArray.getJSONObject(i)
                    .getString(ALPHA2).equals(country)) {
                return jsonArray.getJSONObject(i).getString(language);
            }
        }
        return null;
    }
}
