package controllers;

import dao.interfaces.PostDAO;
import lombok.extern.slf4j.Slf4j;
import model.dbmodel.PostEntity;
import model.dbmodel.PostTypeEntity;
import model.dbmodel.UserEntity;

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
public class CreatePostServlet extends HttpServlet {
    private PostDAO postDAO;

    @Override
    public void init() throws ServletException {
        postDAO = (PostDAO) getServletContext().getAttribute("postDAO");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/createpost.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        UserEntity user = (UserEntity) session.getAttribute("sessionUser");

        String text = req.getParameter("postText");


        int postType = Optional.ofNullable(req.getParameter("postType")).map(Integer::parseInt).orElse(0);


        PostEntity post = new PostEntity();

        post.setText(text);
        post.setPostType(PostTypeEntity.getPostTypeById(postType));
        post.setFromId(user.getId());
        post.setPublishTime(Instant.now());

        postDAO.create(post);

        log.info("postId={}, fromId={}", post.getId(), post.getFromId());

        resp.sendRedirect("/");
    }
}
