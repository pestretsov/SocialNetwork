package controllers;

import lombok.extern.slf4j.Slf4j;
import model.Post;
import model.PostView;
import model.User;
import utils.GeneralUtils;
import utils.SecurityUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

/**
 * Created by artemypestretsov on 8/10/16.
 */

@Slf4j
@WebServlet(urlPatterns = "/postcomments/*")
public class PostCommentsServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        String path = req.getPathInfo().substring(1);

        if (!GeneralUtils.isNumeric(path)) {
            resp.sendError(500, "Post id must be numeric");
            return;
        }

        Optional<PostView> postOpt = Optional.ofNullable(path)
                .map(Integer::parseInt).flatMap(postId -> postViewDAO.getPostViewById(postId));
        Optional<User> sessionUserOpt = SecurityUtils.getSessionUserOpt(session);

        if (!postOpt.isPresent()) {
            resp.sendError(500, "Post with this id doesnt exist");
            return;
        }

        if (sessionUserOpt.isPresent()) {
            req.setAttribute("canComment", true);
        } else {
            req.setAttribute("canComment", false);
        }
        req.setAttribute("postId", postOpt.get().getPostId());

        getServletContext().getRequestDispatcher("/postcomments.jsp").forward(req, resp);
    }
}
