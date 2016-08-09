package controllers;

import dao.interfaces.FollowDAO;
import dao.interfaces.PostDAO;
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
import java.util.Optional;

/**
 * Created by artemypestretsov on 7/29/16.
 */

@Slf4j
@WebServlet(urlPatterns = {"/user/*"})
public class UserServlet extends HttpServlet {
    private UserDAO userDAO;
    private FollowDAO followDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        userDAO = (UserDAO) getServletContext().getAttribute("userDAO");
        followDAO = (FollowDAO) getServletContext().getAttribute("followDAO");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        String path = req.getPathInfo().substring(1);

        Optional<User> userOpt = Optional.ofNullable(path).flatMap(username -> userDAO.getByUsername(username));
        Optional<User> sessionUserOpt = Optional.ofNullable(session).map(s -> (User)s.getAttribute("sessionUser"));

        log.info("trying to get user with username={}", path);

        if (userOpt.isPresent()) {
            log.info("got user with username={}", path);
            req.setAttribute("requestUser", userOpt.get());

            if (sessionUserOpt.isPresent()) {
                int userId = userOpt.get().getId();
                int followId = sessionUserOpt.get().getId();
                req.setAttribute("canFollow", !followDAO.isFollowing(userId, followId));
            } else {
                req.setAttribute("canFollow", false);
            }

            req.setAttribute("followersCount", followDAO.getAllFollowersByUser(userOpt.get().getId()));
            req.setAttribute("followingsCount", followDAO.getAllFollowingsByUser(userOpt.get().getId()));

            getServletContext().getRequestDispatcher("/user.jsp").forward(req, resp);
        } else {
            log.warn("no such user with username={}", path);
            resp.sendError(406, "No user with such username found!");
            return;
        }
    }
}
