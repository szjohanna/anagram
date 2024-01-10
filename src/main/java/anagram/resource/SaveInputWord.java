package anagram.resource;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

@Path("save")
public class SaveInputWord {

    @POST
    @Path("/saveWordPermutations")
    @Produces(MediaType.TEXT_PLAIN)
    public Response hello(@FormParam("inputWord") String word) {
        return Response.seeOther(URI.create("/permutations?inputWord=" + word)).build();
    }

    @POST
    @Path("/saveWordAnagrams")
    @Produces(MediaType.TEXT_PLAIN)
    public Response hello2(@FormParam("inputWord") String word) {
        return Response.seeOther(URI.create("/anagrams?inputWord=" + word)).build();
    }

}
