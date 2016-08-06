package controllers;

import dao.interfaces.UserDAO;
import lombok.extern.slf4j.Slf4j;
import model.User;
import utils.Validator;

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
@WebServlet(urlPatterns = "/secure/changeusername")
public class ChangeUsernameServlet extends HttpServlet {

    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        userDAO = (UserDAO) getServletContext().getAttribute("userDAO");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        User sessionUser = (User)session.getAttribute("sessionUser");
        log.info("user with userId={} is trying to change username", sessionUser.getId());

        String newUsername = req.getParameter("new_username");
        String oldUsername = sessionUser.getUsername();

        if (newUsername.equals(sessionUser.getUsername())) {
            log.info("userId={} new username equals current username", sessionUser.getId());
            resp.sendRedirect("/");
            return;
        }

        if (!Validator.validateUsername(newUsername) || userDAO.getByUsername(newUsername).isPresent()) {
            log.info("userId={} new username={} is invalid or used", sessionUser.getId(), newUsername);
            resp.sendError(500, "Error updating user. Try again");
            return;
        }

        try {
            sessionUser.setUsername(newUsername);

            if (userDAO.update(sessionUser)) {
                log.info("user with userId={} changed username from {} to {}", sessionUser.getId(), oldUsername, newUsername);
                resp.sendRedirect("/");
            } else {
                log.warn("user with userId={} attempt to change username failed", sessionUser.getId());
                resp.sendError(500, "Error updating user. Try again");
            }
        } catch (RuntimeException e) {
            log.warn("user with userId={} attempt to change username failed - exception was thrown", sessionUser.getId());
            resp.sendError(500, "Error updating user. Try again");
        }
    }
}