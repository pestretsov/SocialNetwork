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
import java.util.Optional;

/**
 * Created by artemypestretsov on 8/5/16.
 */

@Slf4j
@WebServlet(urlPatterns = "")
public class IndexServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(true);

        Optional<User> userOpt =
                Optional.ofNullable((User)session.getAttribute("sessionUser"))
                        .map(user -> user.getId())
                        .flatMap(userId -> userDAO.getById(userId));

        if (userOpt.isPresent()) {
            int userId = userOpt.get().getId();
            req.setAttribute("followersCount", followDAO.getAllFollowersByUser(userId));
            req.setAttribute("followingsCount", followDAO.getAllFollowingsByUser(userId));
            log.info("userId={} is present. Redirecting to {}", userId, "/home.jsp");
            getServletContext().getRequestDispatcher("/home.jsp").forward(req, resp);
        } else {
            log.info("no session. Redirecting to {}", "/login.jsp");
            getServletContext().getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }
}
