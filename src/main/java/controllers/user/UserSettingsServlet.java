package controllers.user;

import controllers.BaseServlet;
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
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Optional;

/**
 * Created by artemypestretsov on 8/5/16.
 */

@Slf4j
@WebServlet(urlPatterns = "/secure/usersettings")
public class UserSettingsServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/settings.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(true);

        User sessionUser = (User) session.getAttribute(SESSION_USER);

        String firstName = req.getParameter("first_name");
        String lastName = req.getParameter("last_name");
        String bio = Optional.ofNullable(req.getParameter("bio")).orElse("");

        String birthDate = Optional.ofNullable(req.getParameter("birth_date")).orElse("");

        sessionUser.setFirstName(firstName);
        sessionUser.setLastName(lastName);

        try {
            sessionUser.setBirthDate(LocalDate.parse(birthDate));
        } catch (DateTimeParseException e) {
            LocalDate prevBirthDate = sessionUser.getBirthDate();
            sessionUser.setBirthDate(prevBirthDate);
        }

        sessionUser.setBio(bio);

        try {
            if (userDAO.update(sessionUser)) {
                log.info("user with userId={} was updated. Redirecting to {}", sessionUser.getId(), "/");
                session.setAttribute("sessionUser", sessionUser);
                resp.sendRedirect("/");
            } else {
                log.warn("error updating user with userId={}", sessionUser.getId());
                resp.sendError(500, "Error while updating user");
            }

        } catch (RuntimeException e) {
            log.warn("error updating user with userId={}", sessionUser.getId());
            resp.sendError(500, "Error updating user");
        }
    }
}
