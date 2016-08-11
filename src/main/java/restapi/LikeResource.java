package restapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import dao.interfaces.LikeDAO;
import dao.interfaces.PostDAO;
import dao.interfaces.PostViewDAO;
import dao.interfaces.UserDAO;
import listeners.ServicesInitializer;
import lombok.extern.slf4j.Slf4j;
import model.Like;
import model.User;
import utils.SecurityUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import java.util.Optional;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static utils.RestUtils.toJson;

/**
 * Created by artemypestretsov on 8/8/16.
 */

@Slf4j
@Path("/likes")
public class LikeResource {
    private static LikeDAO likeDAO;

    @Context
    public void init(ServletContext servletContext) {
        if (likeDAO == null) {
            likeDAO = (LikeDAO) servletContext.getAttribute(ServicesInitializer.LIKE_DAO);
        }
    }

    @Context
    HttpServletRequest request;

    @POST
    @Path("/addlike")
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response addLike(Like like) {

        HttpSession session = request.getSession(false);

        Optional<User> sessionUserOpt = SecurityUtils.getSessionUserOpt(session);

        if (!sessionUserOpt.isPresent()) {
            log.warn("No user session found. Cannot add like");
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("User is not signedup").build();
        }

        User sessionUser = sessionUserOpt.get();

        like.setUserId(sessionUser.getId());

        try {
            log.info("userId={} is trying to add like to postId={}", like.getUserId(), like.getPostId());
            if (likeDAO.addLike(like) >= 0) {
                log.info("userId={} has added like to postId={}", like.getUserId(), like.getPostId());
            } else {
                log.warn("userId={} has failed to add like to postId={}", like.getUserId(), like.getPostId());
            }
            return Response.ok().build();
        } catch (RuntimeException e) {
            log.warn("Error adding userId={} like to postId={}, error={}", like.getUserId(), like.getPostId(), e);
            return Response.serverError().entity("Error adding like").build();
        }
    }

    @DELETE
    @Path("/removelike")
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response removeLike(Like like) {

        HttpSession session = request.getSession(false);

        Optional<User> sessionUserOpt = Optional.ofNullable(session)
                .map(s -> (User)s.getAttribute("sessionUser"));

        if (!sessionUserOpt.isPresent()) {
            log.warn("No user session found. Cannot add like");
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("User is not signedup").build();
        }

        User sessionUser = sessionUserOpt.get();

        like.setUserId(sessionUser.getId());

        try {
            log.info("userId={} is trying to delete like from postId={}", like.getUserId(), like.getPostId());
            if (likeDAO.removeLike(like)) {
                log.info("userId={} has deleted like from postId={}", like.getUserId(), like.getPostId());
            } else {
                log.warn("userId={} has failed to delete like from postId={}", like.getUserId(), like.getPostId());
            } return Response.ok().build();
        } catch (RuntimeException e) {
            log.warn("Error adding userId={} like to postId={}, error={}", like.getUserId(), like.getPostId(), e);
            return Response.serverError().entity("Error adding like").build();
        }
    }
}
