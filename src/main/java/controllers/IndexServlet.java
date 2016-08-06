package controllers;

import dao.interfaces.UserDAO;
import lombok.extern.slf4j.Slf4j;
import model.dbmodel.UserEntity;

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
public class IndexServlet extends HttpServlet {

    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        userDAO = (UserDAO) getServletContext().getAttribute("userDAO");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(true);

        Optional<UserEntity> userOpt =
                Optional.ofNullable((UserEntity)session.getAttribute("sessionUser"))
                        .map(user -> user.getId())
                        .flatMap(userId -> userDAO.getById(userId));

        if (userOpt.isPresent()) {
            log.info("userId={} is present. Redirecting to {}", userOpt.get().getId(), "/home.jsp");
            getServletContext().getRequestDispatcher("/home.jsp").forward(req, resp);
        } else {
            log.info("no session. Redirecting to {}", "/login.jsp");
            getServletContext().getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }
}
