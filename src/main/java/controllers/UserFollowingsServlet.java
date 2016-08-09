package controllers;

import dao.interfaces.FollowDAO;
import dao.interfaces.UserDAO;
import lombok.extern.slf4j.Slf4j;
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
 * Created by artemypestretsov on 8/9/16.
 */
@Slf4j
@WebServlet(urlPatterns = "/followings/*")
public class UserFollowingsServlet extends HttpServlet {
    private UserDAO userDAO;
    private FollowDAO followDAO;

    @Override
    public void init() throws ServletException {
        userDAO = (UserDAO) getServletContext().getAttribute("userDAO");
        followDAO = (FollowDAO) getServletContext().getAttribute("followDAO");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        String path = req.getPathInfo().substring(1);

        Optional<User> userOpt = Optional.ofNullable(path).flatMap(username -> userDAO.getByUsername(username));

        if (!userOpt.isPresent()) {
            resp.sendError(500, "no such user");
            return;
        }

        User user = userOpt.get();

        req.setAttribute("followingsList", userDAO.getUsersFollowedByUser(user.getId()));
        getServletContext().getRequestDispatcher("/followings.jsp").forward(req, resp);
    }
}
