package controllers;

import dao.interfaces.FollowDAO;
import dao.interfaces.UserDAO;
import lombok.extern.slf4j.Slf4j;
import model.Follow;
import model.User;

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
public class FollowUserServlet extends BaseServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        User sessionUser = (User)session.getAttribute(SESSION_USER);

        int userToFollowId = Integer.parseInt(req.getParameter("requestUserId"));

        if (userToFollowId == sessionUser.getId()) {
            resp.sendError(500, "Cannot follow yourself");
            return;
        }

        log.info("userId={} is trying to follow userId={}", sessionUser.getId(), userToFollowId);

        try {
            Follow follow = new Follow();
            follow.setUserId(userToFollowId);
            follow.setFollowerId(sessionUser.getId());

            followDAO.create(follow);
        } catch (RuntimeException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error trying to follow user");
            return;
        }
        
        Follow createdFollow = followDAO.getByUserAndFollowerId(userToFollowId, sessionUser.getId());
        User user = userDAO.getById(createdFollow.getUserId()).get();

        log.debug("{} now follows {}", sessionUser.getUsername(), user.getUsername());

        resp.sendRedirect("/");
    }
}
