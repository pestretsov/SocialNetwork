package restapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dao.interfaces.PostDAO;
import dao.interfaces.PostViewDAO;
import dao.interfaces.UserDAO;
import lombok.extern.slf4j.Slf4j;
import model.dbmodel.PostEntity;
import model.dbmodel.PostView;
import model.restmodel.Post;
import model.restmodel.User;

import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Created by artemypestretsov on 7/29/16.
 */

@Slf4j
@Path("/posts")
public class PostResource {
    private static PostDAO postDAO;
    private static UserDAO userDAO;
    private static PostViewDAO postViewDAO;

    @Context
    public void init(ServletContext servletContext) {
        if (postDAO == null) {
            postDAO = (PostDAO) servletContext.getAttribute("postDAO");
        }
        if (userDAO == null) {
            userDAO = (UserDAO) servletContext.getAttribute("userDAO");
        }
        if (postViewDAO == null) {
            postViewDAO = (PostViewDAO) servletContext.getAttribute("postViewDAO");
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

        Optional<PostEntity> postOpt = postDAO.getById(id);

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

    @GET
    @Path("/secure")
    @Produces(APPLICATION_JSON)
    public Response getPersonalTimelineWithOffsetAndLimit(
            @QueryParam("followerId") int followerId,
            @QueryParam("offsetId") int offsetId,
            @QueryParam("limit") int limit) {

        List<PostEntity> posts = postDAO.getPersonalTimelineWithOffsetId(followerId, offsetId, limit);

        List<Post> result = posts.stream()
                .map(p -> new Post(p, new User(userDAO.getById(p.getFromId()).orElse(null))))
                .collect(Collectors.toList());

        try {
            String json = toJson(result);
            return Response.ok(json).build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @GET
    @Produces(APPLICATION_JSON)
    public Response getPostsWithOffsetAndLimit(
            @QueryParam("fromId") int fromId,
            @QueryParam("offsetId") int offsetId,
            @QueryParam("limit") int limit) {

        List<PostView> posts = postViewDAO.getSublist(fromId, offsetId, limit);

        List<Post> result = posts.stream()
                .map(p -> new Post(postDAO.getById(p.getPostId()).get(), new User(userDAO.getById(p.getFromId()).orElse(null))))
                .collect(Collectors.toList());

        try {
            String json = toJson(result);
            return Response.ok(json).build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @DELETE
    @Path("/secure/{id}")
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
    @Path("/secure")
    @Consumes(APPLICATION_JSON)
    public void updatePostById(PostEntity post) {
        log.info("trying to UPDATE post with postId={}", post.getId());
        // TODO: needs fix -- probably new parser
        post.setPublishTime(Instant.now());
        postDAO.update(post);
        log.info("post with postId={} was updated", post.getId());
    }
}
