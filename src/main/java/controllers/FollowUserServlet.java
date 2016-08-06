package controllers;

import dao.interfaces.FollowDAO;
import dao.interfaces.UserDAO;
import lombok.extern.slf4j.Slf4j;
import model.dbmodel.FollowEntity;
import model.dbmodel.UserEntity;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by artemypestretsov on 8/6/16.
 */

@Slf4j
@WebServlet(urlPatterns = "/secure/follow")
public class FollowUserServlet extends HttpServlet {
    private UserDAO userDAO;
    private FollowDAO followDAO;

    @Override
    public void init() throws ServletException {
        userDAO = (UserDAO) getServletContext().getAttribute("userDAO");
        followDAO = (FollowDAO) getServletContext().getAttribute("followDAO");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        UserEntity sessionUser = (UserEntity)session.getAttribute("sessionUser");

        int userToFollowId = Integer.parseInt(req.getParameter("requestUserId"));

        log.info("userId={} is trying to follow userId={}", sessionUser.getId(), userToFollowId);

        try {
            FollowEntity follow = new FollowEntity();
            follow.setUserId(userToFollowId);
            follow.setFollowerId(sessionUser.getId());

            followDAO.create(follow);
        } catch (RuntimeException e) {
            resp.sendError(500, "Something went wrong");
            return;
        }
        FollowEntity createdFollow = followDAO.getByUserAndFollowerId(userToFollowId, sessionUser.getId());
        UserEntity user = userDAO.getById(createdFollow.getUserId()).get();

        log.debug("{} now follows {}", sessionUser.getUsername(), user.getUsername());

        resp.sendRedirect("/");
    }
}
