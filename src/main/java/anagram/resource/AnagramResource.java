package anagram.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

@Path("hello")
public class AnagramResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response callAPI() throws Exception {

        URL url = new URL("https://api.dictionaryapi.dev/api/v2/entries/en/cat");

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();

        //Check if connect is made
        int responseCode = conn.getResponseCode();

        //200 - OK
        if (responseCode != 200) {
            throw new RuntimeException("HttpResponseCode: " + responseCode);
        } else {

            StringBuilder informationString = new StringBuilder();
            Scanner scanner = new Scanner(url.openStream());

            while (scanner.hasNext()) {
                informationString.append(scanner.nextLine());
            }
            scanner.close();

            System.out.println(informationString);

            //Save data gotten from API into variable dataObject
            JSONParser parse = new JSONParser();
            JSONArray dataObject = (JSONArray) parse.parse(String.valueOf(informationString));

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
            ArrayList<String> propertiesOfWord = new ArrayList<String>();
            propertiesOfWord.add(type);
            propertiesOfWord.add(description);
            propertiesOfWord.add(pronounciation);


            return Response.ok(propertiesOfWord).build();

        }

    }

    private Object getKey(JSONArray array, String key)
    {
        Object value = null;
        //boolean keyFound = false;
        for (int i = 0; i < array.size(); i++)
        {
            JSONObject item = (JSONObject) array.get(i);
            if (item.keySet().contains(key)) //  && !keyFound
            {
                value = item.get(key);
                //System.out.println("Key found at index " + i);
                //keyFound = true;
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
