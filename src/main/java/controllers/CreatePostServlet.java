package controllers;

import dao.interfaces.PostDAO;
import dao.interfaces.UserDAO;
import lombok.extern.slf4j.Slf4j;
import model.Post;
import model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;

/**
 * Created by artemypestretsov on 7/29/16.
 */

@Slf4j
@WebServlet(urlPatterns = "/createpost")
public class CreatePostServlet extends HttpServlet {
    private PostDAO postDAO;

    @Override
    public void init() throws ServletException {
        postDAO = (PostDAO) getServletContext().getAttribute("postDAO");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/create_post.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        User user = (User) session.getAttribute("sessionUser");

        String text = req.getParameter("postText");

        Post post = new Post();

        post.setText(text);
        post.setPostType(0);
        post.setFromId(user.getId());
        post.setPublishTime(Instant.now());

        postDAO.create(post);

        log.info("postId={}, fromId={}", post.getId(), post.getFromId());

        resp.sendRedirect("/");
    }
}
