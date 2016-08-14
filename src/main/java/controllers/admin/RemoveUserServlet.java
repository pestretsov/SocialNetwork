package controllers.admin;

import controllers.BaseServlet;
import lombok.extern.slf4j.Slf4j;
import utils.GeneralUtils;
import utils.SecurityUtils;

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
@WebServlet(urlPatterns = "/admin/removeuser")
public class RemoveUserServlet extends BaseServlet {
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

        boolean isSameUser = SecurityUtils.getSessionUserOpt(req.getSession()).get().getId()==userId;

        if (isSameUser) {
            log.warn("trying to delete yourself. Redirecting to error page");
            resp.sendError(406, "You can't delete yourself");
            return;
        }


        log.info("trying to delete user with userId={}", userId);
        try {
            if (userDAO.deleteById(userId)){
                log.info("user with userId={} was deleted", userId);
            } else {
                log.warn("user with userId={} was not deleted", userId);
            }

            log.info("redirecting to /admin/adminpage");
            resp.sendRedirect("/admin/adminpage");

        } catch (RuntimeException e) {
            log.warn("error trying to delete user. Redirect to error page. Error={}",e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error trying to delete user");
        }
    }
}
