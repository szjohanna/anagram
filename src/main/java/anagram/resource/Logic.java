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

        //If all characters from 'String str' (which is actually 'ros' from the recursive call)
        //have been used up to create a new permutation:
        if (str.length() == 0) {

            //Add new permutation to Model.allpermutations
            System.out.print(ans + " ");
            Model.getAllpermutations().add(ans);
            return;
        }

        boolean alpha[] = new boolean[26];
        //Go through all characters of str
        for (int i = 0; i < str.length(); i++) {

            //ch = current character it's iterating over
            char ch = str.charAt(i);

            //ros = version of str not containing 'ch'
            String ros = str.substring(0, i) +
                    str.substring(i + 1);

            //If 'ch' has not been used in the permutations generated so far
            if (alpha[ch - 'a'] == false) {
                //Call function recursively by appending 'ch' to the partial permutation (ans)
                printDistinctPermutations(ros, ans + ch);
                alpha[ch - 'a'] = true; //Has generated permutations with the current 'ch'
            }
        }

    }

    static void checkForAnagrams() throws Exception {

        for(int i = 0; i < Model.getAllpermutations().size(); i++) {

            String baseUrlPath = "https://api.dictionaryapi.dev/api/v2/entries/en/";
            //Create URL for each permutation
            String urlPath = baseUrlPath + Model.getAllpermutations().get(i);
            URL url = new URL(urlPath);
            //Open connection to API
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            //If the responseCode is 200, that means the API found the permutation -> it's an existing word
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                //Add the anagram to the Model.anagrams ArrayList
                Model.getAnagrams().add(Model.getAllpermutations().get(i));
            }

        }

    }


    static void filterData() throws Exception {

        for (int i = 0; i < Model.getAnagrams().size(); i++) {

            String baseUrlPath = "https://api.dictionaryapi.dev/api/v2/entries/en/";
            //Create URL for each permutation
            String urlPath = baseUrlPath + Model.getAnagrams().get(i);
            URL url = new URL(urlPath);
            //Open connection to API
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            //Collect data from the API
            StringBuilder informationString = new StringBuilder();
            Scanner scanner = new Scanner(url.openStream());

            while (scanner.hasNext()) {
                informationString.append(scanner.nextLine());
            }
            scanner.close();

            //JSON simple library Setup with Maven: used to convert strings to JSON
            JSONParser parse = new JSONParser();
            JSONArray dataObject = (JSONArray) parse.parse(String.valueOf(informationString));

            //Going through the multidimensional JSONArrays using their keys to reach the values needed
            String type = getKeyValue((JSONArray) getKeyValue(dataObject, "meanings"), "partOfSpeech").toString();
            String description =  getKeyValue((JSONArray) getKeyValue((JSONArray) getKeyValue(dataObject, "meanings"), "definitions"), "definition").toString();
            String pronunciation = getAllKeyValues((JSONArray) getKeyValue(dataObject, "phonetics"), "audio").toString();

            //This is the filtered data needed for the anagrams: type, description, pronunciation
            ArrayList<String> data = new ArrayList<String>();
            data.add(Model.getAnagrams().get(i).toString()); // + the anagram itself
            data.add(type);
            data.add(description);
            data.add(pronunciation);

            //Save each anagram's information in the Model.anagramdata ArrayList using anagramsinfo
            ArrayList<Object> anagramsinfo = new ArrayList<Object>();
            anagramsinfo.add(data);
            Model.setAnagramdata(anagramsinfo);
        }

    }

    private static Object getKeyValue(JSONArray array, String key) {
        Object value = null;
        //Goes through all JSONArray objects to check if it contains the key provided
        for (int i = 0; i < array.size(); i++) {
            JSONObject item = (JSONObject) array.get(i);
            if (item.keySet().contains(key)) {
                value = item.get(key);
                break;
            }
        }
        return value;
    }

    private static List<Object> getAllKeyValues(JSONArray array, String key) {
        List<Object> values = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            JSONObject item = (JSONObject) array.get(i);
            //If there are multiple such keys, collect all of them and return all in a List
            if (item.keySet().contains(key))  {
                values.add(item.get(key));
            }
        }
        return values;
    }


}
