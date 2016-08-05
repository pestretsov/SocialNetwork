package controllers;

import dao.interfaces.UserDAO;
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

        Optional<User> userOpt =
                Optional.ofNullable((User)session.getAttribute("sessionUser"))
                        .map(user -> user.getId())
                        .flatMap(userId -> userDAO.getById(userId));

        if (userOpt.isPresent()) {
            getServletContext().getRequestDispatcher("/secure").forward(req, resp);
        } else {
            getServletContext().getRequestDispatcher("/login.jsp").forward(req, resp);
        }

    }

}
