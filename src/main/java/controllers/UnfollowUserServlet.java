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
 * Created by artemypestretsov on 8/9/16.
 */
@Slf4j
@WebServlet(urlPatterns = "/secure/unfollow")
public class UnfollowUserServlet extends BaseServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        User sessionUser = (User) session.getAttribute(SESSION_USER);

        int userToUnfollowId = Integer.parseInt(req.getParameter("requestUserId"));

        log.info("userId={} is trying to unfollow userId={}", sessionUser.getId(), userToUnfollowId);

        try {
            Follow follow = new Follow();
            follow.setUserId(userToUnfollowId);
            follow.setFollowerId(sessionUser.getId());

            if (!followDAO.delete(follow)) {
                log.warn("userId={} has failed to unfollow userId={}", sessionUser.getId(), userToUnfollowId);
                resp.sendError(500, "Something went wrong");
                return;
            }
        } catch (RuntimeException e) {
            log.warn("userId={} has failed to unfollow userId={} thrown exception={}", sessionUser.getId(), userToUnfollowId, e);
            resp.sendError(500, "Something went wrong");
            return;
        }

        log.debug("{} now doesnt follow {}", sessionUser.getUsername(), userToUnfollowId);

        resp.sendRedirect("/");
    }
}
