package anagram.resource;

import anagram.model.Model;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("permutations")
public class PermutationEndpoint {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkPermutations(@QueryParam("inputWord") String word) {

        Model.getAllpermutations().clear(); //if rerun: previous values should be deleted
        new Logic().printDistinctPermutations(word, "");
        return Response.ok(Model.getAllpermutations()).build();

    }

}
