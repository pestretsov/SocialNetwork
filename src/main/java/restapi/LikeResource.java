package restapi;

import dao.interfaces.LikeDAO;
import dao.interfaces.PostDAO;
import dao.interfaces.PostViewDAO;
import dao.interfaces.UserDAO;
import lombok.extern.slf4j.Slf4j;
import model.Like;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

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

    @POST
    @Consumes(APPLICATION_JSON)
    public Response addLike(Like like) {
        likeDAO.
    }

}
