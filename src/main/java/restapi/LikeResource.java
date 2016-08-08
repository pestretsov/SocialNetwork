package restapi;

import com.sun.deploy.net.HttpRequest;
import dao.interfaces.LikeDAO;
import dao.interfaces.PostDAO;
import dao.interfaces.PostViewDAO;
import dao.interfaces.UserDAO;
import lombok.extern.slf4j.Slf4j;
import model.Like;
import model.User;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import java.util.Optional;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Created by artemypestretsov on 8/8/16.
 */

@Slf4j
@Path("/likes")
public class LikeResource {
    private static PostDAO postDAO;
    private static UserDAO userDAO;
    private static PostViewDAO postViewDAO;
    private static LikeDAO likeDAO;

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
        if (likeDAO == null) {
            likeDAO = (LikeDAO) servletContext.getAttribute("likeDAO");
        }
    }

    @Context
    HttpServletRequest request;

    @POST
    @Path("/addlike")
    @Consumes(APPLICATION_JSON)
    public Response addLike(Like like) {

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
            log.info("userId={} it trying to add like to postId={}", like.getUserId(), like.getPostId());
            if (likeDAO.addLike(like) >= 0) {
                log.info("userId={} has added like to postId={}", like.getUserId(), like.getPostId());
            } else {
                log.warn("userId={} has failed to add like to postId={}", like.getUserId(), like.getPostId());
            }
            return Response.ok().build();
        } catch (RuntimeException e) {
            log.warn("Error adding userId={} like to postId={}", like.getUserId(), like.getPostId());
            return Response.serverError().entity("Error adding like").build();
        }
    }

}
