package controllers;

import dao.interfaces.PostDAO;
import lombok.extern.slf4j.Slf4j;
import model.Post;
import model.PostType;
import model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.Instant;
import java.util.Optional;

/**
 * Created by artemypestretsov on 7/29/16.
 */

@Slf4j
@WebServlet(urlPatterns = "/secure/createpost")
public class CreatePostServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/createpost.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        User user = (User) session.getAttribute("sessionUser");

        String text = req.getParameter("postText");


        PostType postType = Optional.ofNullable(req.getParameter("postType")).map(PostType::valueOf).orElse(PostType.DEFAULT);


        Post post = new Post();

        post.setText(text);
        post.setPostType(postType);
        post.setFromId(user.getId());
        post.setPublishTime(Instant.now());

        post.setId(postDAO.create(post));

        log.info("postId={}, postType={}, fromId={}", post.getId(), postType,post.getFromId());

        resp.sendRedirect("/");
    }
}
