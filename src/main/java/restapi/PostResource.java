package restapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dao.interfaces.PostDAO;
import javafx.geometry.Pos;
import model.Post;

import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import java.util.List;
import java.util.Optional;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Created by artemypestretsov on 7/29/16.
 */

@Path("/posts")
public class PostResource {
    private static PostDAO postDAO;

    @Context
    public void init(ServletContext servletContext) {
//        System.out.println("here init");
        if (postDAO == null)
            postDAO = (PostDAO) servletContext.getAttribute("postDAO");
    }

    public String toJson(Object object) throws JsonProcessingException {
        return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(object);
    }

    @GET
    @Path("{id}")
    @Produces(APPLICATION_JSON)
    public Response getPostById(@PathParam("id") int id) {

        Optional<Post> postOpt = postDAO.getById(id);

        try {
            if (postOpt.isPresent()) {
                String json = toJson(postOpt.get());
                return Response.ok(json).build();
            } else {
                return Response.noContent().build();
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @GET
    @Produces(APPLICATION_JSON)
    public Response getPostsWithOffsetAndLimit(
            @QueryParam("fromId") int fromId,
            @QueryParam("offset") int offset,
            @QueryParam("limit") int limit) {

        List<Post> posts = postDAO.getSublistByFromId(fromId, offset, limit);

        try {
            String json = toJson(posts);
            return Response.ok(json).build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @DELETE
    @Path("{id}")
    public void deletePostById(@PathParam("id") int id) {
        if (postDAO.getById(id).isPresent()) {
            postDAO.deleteById(id);
        } else {
            throw new RuntimeException("Resource with such path is unavailable");
        }
    }
}
