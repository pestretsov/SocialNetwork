package controllers;

import lombok.extern.slf4j.Slf4j;
import model.User;
import utils.SecurityUtils;

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
@WebServlet(urlPatterns = "/discover")
public class DiscoverServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);

        Optional<User> userOpt = SecurityUtils.getSessionUserOpt(session);

        if (userOpt.isPresent()) {
            req.setAttribute("discoverUsersList", userDAO.getUsersNotFollowedByUser(userOpt.get().getId()));
        } else {
            req.setAttribute("discoverUsersList", userDAO.getAllUsers());
        }

        getServletContext().getRequestDispatcher("/discover.jsp").forward(req, resp);
    }
}
