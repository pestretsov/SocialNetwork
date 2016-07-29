package restapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dao.interfaces.PostDAO;
import javafx.geometry.Pos;
import model.Post;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import java.util.Optional;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Created by artemypestretsov on 7/29/16.
 */

@Path("/post")
@Produces(APPLICATION_JSON)
public class PostResource {
    private static PostDAO postDAO;

    @Context
    public void init(ServletContext servletContext) {
        System.out.println("here init");
        if (postDAO == null)
            postDAO = (PostDAO) servletContext.getAttribute("postDAO");
    }

    @GET
    @Path("{id}")
    public Response getPostById(@PathParam("id") int id) {
        ObjectMapper mapper = new ObjectMapper();

        System.out.println("here get");

        Optional<Post> postOpt = postDAO.getById(id);

        try {
            if (postOpt.isPresent()) {
                String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(postOpt.get());
                return Response.ok(json).build();
            } else {
                return Response.noContent().build();
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
