package anagram.resource;

import anagram.model.Model;

import java.net.HttpURLConnection;
import java.net.URL;

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


}
