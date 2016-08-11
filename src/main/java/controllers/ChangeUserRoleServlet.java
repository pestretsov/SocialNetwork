package controllers;

import lombok.extern.slf4j.Slf4j;
import model.User;
import model.UserRole;
import utils.GeneralUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * Created by artemypestretsov on 8/11/16.
 */

@Slf4j
@WebServlet(urlPatterns = "/admin/changerole")
public class ChangeUserRoleServlet extends BaseServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String userIdString = req.getParameter("user_id");

        if (!GeneralUtils.isNumeric(userIdString)) {
            log.warn("no such user");
            resp.sendError(500, "Error: no such username");
            return;
        }

        int userId = Optional.ofNullable(userIdString).map(Integer::parseInt).orElse(-1);

        if (userId == -1) {
            log.info("invalid user_id param. Redirecting to {}", "/admin/adminpage");
            resp.sendRedirect("/admin/adminpage");
            return;
        }

        Optional<User> userOpt = userDAO.getById(userId);

        if (!userOpt.isPresent()) {
            log.warn("no such user");
            resp.sendError(404, "no such user");
            return;
        }

        User user = userOpt.get();

        if (user.getRole() == UserRole.USER) {
            user.setRole(UserRole.ADMIN);
        } else {
            user.setRole(UserRole.USER);
        }

        try {
            if (userDAO.update(user)){
                log.info("updated userId={} role={}", user.getId(), user.getRole());
                resp.sendRedirect("/admin/adminpage");
            } else {
                log.warn("update has failed. No such user found. Redirecting to error page");
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Update has failed");
            }
        } catch (RuntimeException e) {
            log.warn("Error trying to update user. Redirect to error page error={}", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error trying to delete user");
        }
    }
}
