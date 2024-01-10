package anagram.resource;

import anagram.model.Model;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Logic {

    static void printDistinctPermutations(String str, String ans) {

        //If the word's character string is empty
        if (str.length() == 0) {

            //print permutations as of right now
            System.out.print(ans + " ");
            Model.getAllpermutations().add(ans);
            return;
        }

        boolean alpha[] = new boolean[26];

        for (int i = 0; i < str.length(); i++) {

            // ith character of str
            char ch = str.charAt(i);

            //Rest of the string after excluding the ith character
            String ros = str.substring(0, i) +
                    str.substring(i + 1);

            //If the character has not been used -> recursive call
            if (alpha[ch - 'a'] == false) {
                printDistinctPermutations(ros, ans + ch);
                alpha[ch - 'a'] = true;
            }
        }

    }

    static void checkForAnagrams() throws Exception {

        for(int i = 0; i < Model.getAllpermutations().size(); i++) {

            String baseUrlPath = "https://api.dictionaryapi.dev/api/v2/entries/en/";

            String urlPath = baseUrlPath + Model.getAllpermutations().get(i);
            System.out.println("This is the URL PATH: "+urlPath);
            URL url = new URL(urlPath);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            int responseCode = conn.getResponseCode();
            System.out.println("This is the responsecode: "+responseCode);
            if (responseCode == 200) {
                Model.getAnagrams().add(Model.getAllpermutations().get(i));
            }

        }

    }


    static void filterData() throws Exception {

        for (int i = 0; i < Model.getAnagrams().size(); i++) {

            String baseUrlPath = "https://api.dictionaryapi.dev/api/v2/entries/en/";

            String urlPath = baseUrlPath + Model.getAnagrams().get(i);
            System.out.println("This is the URL PATH: "+urlPath);
            URL url = new URL(urlPath);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            System.out.println("Connection responsecode: "+conn.getResponseCode());

            StringBuilder informationString = new StringBuilder();
            Scanner scanner = new Scanner(url.openStream());

            while (scanner.hasNext()) {
                informationString.append(scanner.nextLine());
            }
            //Close the scanner
            scanner.close();

            //JSON simple library Setup with Maven is used to convert strings to JSON
            JSONParser parse = new JSONParser();
            JSONArray dataObject = (JSONArray) parse.parse(String.valueOf(informationString));

            //Figure out a way to make this code more efficient
            JSONArray firstRoundDefinition = (JSONArray)getKey(dataObject, "meanings");
            JSONArray secondRoundDefinition = (JSONArray)getKey(firstRoundDefinition, "definitions");
            System.out.println("I AM LOOKING FOR THIS NOW: "+getKey(secondRoundDefinition, "definition"));
            String description = getKey(secondRoundDefinition, "definition").toString();

            JSONArray firstRoundType = (JSONArray)getKey(dataObject, "meanings");
            System.out.println("I AM LOOKING FOR THIS NOW2: "+getKey(firstRoundType, "partOfSpeech"));
            String type = getKey(firstRoundType, "partOfSpeech").toString();

            JSONArray firstRoundPronounciation = (JSONArray)getKey(dataObject, "phonetics");
            List<Object> values = getKeys2(firstRoundPronounciation, "audio");
            String pronounciation = values.toString();
            System.out.println("I AM LOOKING FOR THIS NOW3: "+values);

            //This is the filtered data I needed to collect for the anagrams
            ArrayList<String> data = new ArrayList<String>();
            data.add(Model.getAnagrams().get(i).toString());
            data.add(type);
            data.add(description);
            data.add(pronounciation);

            //Store each anagram's information in this Object
            ArrayList<Object> anagramsinfo = new ArrayList<Object>();

            anagramsinfo.add(data);
            Model.setAnagramdata(anagramsinfo);


        }

    }

    private static Object getKey(JSONArray array, String key) {
        Object value = null;
        for (int i = 0; i < array.size(); i++)
        {
            JSONObject item = (JSONObject) array.get(i);
            if (item.keySet().contains(key))
            {
                value = item.get(key);
                break;
            }
        }

        return value;
    }

    private static List<Object> getKeys2(JSONArray array, String key) {
        List<Object> values = new ArrayList();
        for (int i = 0; i < array.size(); i++) {
            JSONObject item = (JSONObject) array.get(i);
            if (item.keySet().contains(key))  {
                values.add(item.get(key));
            }
        }

        return values;
    }


}
