package controllers;

import dao.interfaces.UserDAO;
import lombok.extern.slf4j.Slf4j;
import model.dbmodel.UserEntity;
import utils.SecurityUtils;

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
@WebServlet(urlPatterns = "/secure/changepassword")
public class ChangePasswordServlet extends HttpServlet {
    private UserDAO userDAO;
    private SecurityUtils securityUtils;

    @Override
    public void init() throws ServletException {
        userDAO = (UserDAO) getServletContext().getAttribute("userDAO");
        securityUtils = (SecurityUtils) getServletContext().getAttribute("securityUtils");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        UserEntity sessionUser = (UserEntity)session.getAttribute("sessionUser");
        log.info("user with userId={} is trying to change password", sessionUser.getUsername());

        String oldPassword = req.getParameter("old_password");

        if (!securityUtils.validatePassword(oldPassword, sessionUser.getPassword())) {
            log.warn("old password is incorrect");
            resp.sendError(406, "Old password is incorrect");
            return;
        }

        String newPassword = req.getParameter("new_password");
        String newPasswordTest = req.getParameter("new_password_confirm");

        if (newPassword == null || newPassword.isEmpty() || !newPassword.equals(newPasswordTest)) {
            log.info("user with userId={} new password is invalid", sessionUser.getId());
            resp.sendError(406, "New password is incorrect");
            return;
        }

        try {
            sessionUser.setPassword(securityUtils.encrypt(newPassword));
            if (userDAO.update(sessionUser)) {
                log.info("user with userId={} updated password. Redirecting to {}", sessionUser.getId(),"/");
                session.setAttribute("sessionUser", sessionUser);
                resp.sendRedirect("/");
            } else {
                log.warn("user with userId={} password update failed");
                resp.sendError(500, "Error updating password. Try again");
            }
        } catch (RuntimeException e) {
            log.warn("user with userId={} password update was unsuccessful", sessionUser.getId());
            resp.sendError(500, "Error updating user. Try again");
        }
    }
}
