package restapi;

import dao.interfaces.CommentDAO;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static utils.RestUtils.toJson;

/**
 * Created by artemypestretsov on 8/9/16.
 */
@Slf4j
@Path("/comments")
public class CommentResource {
    private static CommentDAO commentDAO;

    @Context
    public void init(ServletContext servletContext) {
        if (commentDAO == null) {
            commentDAO = (CommentDAO) servletContext.getAttribute("commentDAO");
        }
    }

    @GET
    @Path("/getcomments")
    @Produces(APPLICATION_JSON)
    public Response getPostComments(@QueryParam("postId") int postId,
                                    @QueryParam("offsetId") int offsetId,
                                    @QueryParam("limit") int limit) {
        return Response.ok().build();
    }

}
