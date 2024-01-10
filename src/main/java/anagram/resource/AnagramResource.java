package anagram.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

@Path("hello")
public class AnagramResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response callAPI() throws Exception {

        URL url = new URL("https://api.dictionaryapi.dev/api/v2/entries/en/horse");

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

            return Response.ok(dataObject).build();

        }

    }


}
