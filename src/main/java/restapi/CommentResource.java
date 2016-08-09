package restapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import dao.interfaces.CommentDAO;
import dao.interfaces.CommentViewDAO;
import lombok.extern.slf4j.Slf4j;
import model.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static utils.RestUtils.toJson;

/**
 * Created by artemypestretsov on 8/9/16.
 */
@Slf4j
@Path("/comments")
public class CommentResource {
    private static CommentDAO commentDAO;
    private static CommentViewDAO commentViewDAO;

    @Context
    public void init(ServletContext servletContext) {
        if (commentDAO == null) {
            commentDAO = (CommentDAO) servletContext.getAttribute("commentDAO");
        }

        if (commentViewDAO == null) {
            commentDAO = (CommentDAO) servletContext.getAttribute("commentViewDAO");
        }
    }

    @Context
    HttpServletRequest request;

    @GET
    @Path("/getcomments")
    @Produces(APPLICATION_JSON)
    public Response getPostComments(@QueryParam("postId") int postId,
                                    @QueryParam("offsetId") int offsetId,
                                    @QueryParam("limit") int limit) {

        // TODO: post-privacy & follow
        List<CommentView> comments = commentViewDAO.getPostCommentsSublist(postId, offsetId, limit);

        try {
            String json = toJson(comments);
            log.debug("someone requested comments to postId={}", postId);
            return Response.ok(json).build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @POST
    @Path("/addcomment")
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response addComment(Comment comment) {

        HttpSession session = request.getSession(false);

        Optional<User> sessionUserOpt = Optional.ofNullable(session)
                .map(s -> (User)s.getAttribute("sessionUser"));

        if (!sessionUserOpt.isPresent()) {
            log.warn("No user session found. Cannot add comment");
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("User is not signedup").build();
        }

        try {
            log.info("userId={} is trying to add comment to postId={}", comment.getFromId(), comment.getPostId());
            if (commentDAO.addPostComment(comment) >= 0) {
                log.info("userId={} has added comment to postId={}", comment.getFromId(), comment.getPostId());
            } else {
                log.warn("userId={} has failed to add like to postId={}", comment.getFromId(), comment.getPostId());
            }
            return Response.ok().build();
        } catch (RuntimeException e) {
            log.warn("Error adding userId={} like to postId={}, error={}", comment.getFromId(), comment.getPostId(), e);
            return Response.serverError().entity("Error adding like").build();
        }
    }
}
