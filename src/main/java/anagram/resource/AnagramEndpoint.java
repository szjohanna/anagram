package anagram.resource;

import anagram.model.Model;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("anagrams")
public class AnagramEndpoint {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkForAnagarams(@QueryParam("inputWord") String word) throws Exception {


        //Create the permutation List
        Model.getAllpermutations().clear();
        new Logic().printDistinctPermutations(word, "");

        //Call method that checks for anagrams -> anagrams List
        Logic.checkForAnagrams();
        //Loop through anagrams List and get info of each -> save to anagraminfo List
        Logic.filterData();

        return Response.ok(Model.getAnagramdata()).build();
    }


}
