package restapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import dao.interfaces.FollowDAO;
import dao.interfaces.PostDAO;
import dao.interfaces.PostViewDAO;
import dao.interfaces.UserDAO;
import lombok.extern.slf4j.Slf4j;
import model.Post;
import model.PostType;
import model.PostView;
import model.User;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static utils.SecurityUtils.getSessionUserOpt;
import static utils.RestUtils.toJson;

/**
 * Created by artemypestretsov on 7/29/16.
 */

@Slf4j
@Path("/posts")
public class PostResource {
    private static PostDAO postDAO;
    private static UserDAO userDAO;
    private static FollowDAO followDAO;
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
        if (followDAO == null) {
            followDAO = (FollowDAO) servletContext.getAttribute("followDAO");
        }
    }

    @Context
    HttpServletRequest request;

    @GET
    @Path("/{id}")
    @Produces(APPLICATION_JSON)
    public Response getPostById(@PathParam("id") int id) {

        log.info("trying to GET post with postId={}", id);

        Optional<Post> postOpt = postDAO.getById(id);

        if (!postOpt.isPresent()) {
            log.warn("no such post with postId={}", id);
            return Response.noContent().build();
        }

        Post post = postOpt.get();

        if (post.getPostType() == PostType.PRIVATE) {
            Optional<User> sessionUser = getSessionUserOpt(request.getSession());
            if (!sessionUser.isPresent()) {
                log.warn("No user session found. Cannot get private post");
                return Response.status(Response.Status.FORBIDDEN)
                        .entity("User is not signedup").build();
            } else {
                if (!followDAO.isFollowing(post.getFromId(), sessionUser.get().getId())) {
                    return Response.status(Response.Status.FORBIDDEN)
                            .entity("Cannot see private posts").build();
                }
            }
        }

        try {
            log.info("got post with postId={}", id);
            String json = toJson(post);
            return Response.ok(json).build();
        } catch (JsonProcessingException e) {
            log.warn("JsonProcessingException thrown trying to get post with postId={}", id);
            throw new RuntimeException(e);
        }
    }

    @GET
    @Path("/gettimeline")
    @Produces(APPLICATION_JSON)
    public Response getPersonalTimelineWithOffsetAndLimit(
            @QueryParam("followerId") int followerId,
            @QueryParam("offsetId") int offsetId,
            @QueryParam("limit") int limit) {

        if (!getSessionUserOpt(request.getSession()).isPresent()) {
            log.warn("No user session found. Cannot get timeline");
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("User is not signedup").build();
        }

        List<PostView> posts = postViewDAO.getPersonalTimeline(followerId, offsetId, limit);

        try {
            String json = toJson(posts);
            log.debug("userId={} requested timeline", followerId);
            return Response.ok(json).build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @GET
    @Produces(APPLICATION_JSON)
    public Response getUserPostsSublist(
            @QueryParam("userId") int userId,
            @QueryParam("fromId") int fromId,
            @QueryParam("offsetId") int offsetId,
            @QueryParam("limit") int limit) {


        List<PostView> posts = postViewDAO.getUserPostsSublist(userId, fromId, offsetId, limit);

        if (!followDAO.isFollowing(userId, fromId) && userId != fromId) {
            posts = posts.stream().filter(pv -> pv.getPostType() != PostType.PRIVATE).collect(Collectors.toList());
        }

        try {
            String json = toJson(posts);
            log.debug("userId={} requested sublist of userId={}", userId, fromId);
            return Response.ok(json).build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @DELETE
    @Path("/delete/{id}")
    public Response deletePostById(@PathParam("id") int id) {

        if (!getSessionUserOpt(request.getSession()).isPresent()) {
            log.warn("No user session found. Cannot add like");
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("User is not signedup").build();
        }

        log.info("trying to DELETE post with postId={}", id);
        if (postDAO.getById(id).isPresent()) {
            postDAO.deleteById(id);
            log.info("post with postId={} was deleted", id);
            return Response.ok().build();
        } else {
            log.info("no such post with postId={}", id);
            throw new RuntimeException("Resource with such path is unavailable");
        }
    }

    @PUT
    @Path("/update")
    @Consumes(APPLICATION_JSON)
    public Response updatePostById(Post post) {

        if (!getSessionUserOpt(request.getSession()).isPresent()) {
            log.warn("No user session found. Cannot add like");
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("User is not signedup").build();
        }

        try {
            log.info("trying to UPDATE post with postId={}", post.getId());
            postDAO.update(post);
            log.info("post with postId={} was updated", post.getId());
            return Response.ok().build();
        } catch (RuntimeException e) {
            return Response.serverError().entity("Error updating post").build();
        }
    }
}
