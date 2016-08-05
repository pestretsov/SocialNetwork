package restapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dao.interfaces.PostDAO;
import lombok.extern.slf4j.Slf4j;
import model.Post;

import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Created by artemypestretsov on 7/29/16.
 */

@Slf4j
@Path("/posts")
public class PostResource {
    private static PostDAO postDAO;

    @Context
    public void init(ServletContext servletContext) {
        if (postDAO == null) {
            postDAO = (PostDAO) servletContext.getAttribute("postDAO");
        }
    }

    public String toJson(Object object) throws JsonProcessingException {
        return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(object);
    }

    @GET
    @Path("{id}")
    @Produces(APPLICATION_JSON)
    public Response getPostById(@PathParam("id") int id) {

        log.info("trying to GET post with postId={}", id);

        Optional<Post> postOpt = postDAO.getById(id);

        try {
            if (postOpt.isPresent()) {
                log.info("got post with postId={}", id);
                String json = toJson(postOpt.get());
                return Response.ok(json).build();
            } else {
                log.warn("no such post with postId={}", id);
                return Response.noContent().build();
            }
        } catch (JsonProcessingException e) {
            log.warn("JsonProcessingException thrown trying to get post with postId={}", id);
            throw new RuntimeException(e);
        }
    }

//    @GET
//    @Produces(APPLICATION_JSON)
//    public Response getPersonalTimelineWithOffsetAndLimit(
//            @QueryParam("followerId") int followerId,
//            @QueryParam("offsetId") int offsetId,
//            @QueryParam("limit") int limit) {
//
//        List<Post> posts = postDAO.getPersonalTimelineWithOffsetId(followerId, offsetId, limit);
//
//        try {
//            String json = toJson(posts);
//            return Response.ok(json).build();
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//    }

    @GET
    @Produces(APPLICATION_JSON)
    public Response getPostsWithOffsetAndLimit(
            @QueryParam("fromId") int fromId,
            @QueryParam("offsetId") int offsetId,
            @QueryParam("limit") int limit) {

        List<Post> posts = postDAO.getSublistWithOffsetId(fromId, offsetId, limit);

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
        log.info("trying to DELETE post with postId={}", id);
        if (postDAO.getById(id).isPresent()) {
            log.info("post with postId={} was deleted", id);
            postDAO.deleteById(id);
        } else {
            log.info("no such post with postId={}", id);
            throw new RuntimeException("Resource with such path is unavailable");
        }
    }

    @PUT
    @Consumes(APPLICATION_JSON)
    public void updatePostById(Post post) {
        log.info("trying to UPDATE post with postId={}", post.getId());
        // TODO: needs fix -- probably new parser
        post.setPublishTime(Instant.now());
        postDAO.update(post);
        log.info("post with postId={} was updated", post.getId());
    }
}
