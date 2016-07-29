package controllers;

import dao.interfaces.PostDAO;
import dao.interfaces.UserDAO;
import javafx.geometry.Pos;
import model.User;
import utils.Validator;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * Created by artemypestretsov on 7/29/16.
 */
@WebServlet(urlPatterns = "/user/*")
public class UserServlet extends HttpServlet {
    private UserDAO userDAO;
    private PostDAO postDAO;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        userDAO = (UserDAO) getServletContext().getAttribute("userDAO");
        postDAO = (PostDAO) getServletContext().getAttribute("postDAO");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo().substring(1);
        Optional<User> userOpt = Optional.ofNullable(path).flatMap(username -> userDAO.getByUsername(username));

        if (userOpt.isPresent()) {
            req.setAttribute("requestUser", userOpt.get());
            req.setAttribute("userPosts", postDAO.getAllByFromId(userOpt.get().getId()));
            req.getRequestDispatcher("/user.jsp").forward(req, resp);
        } else {
            resp.sendError(406, "No user with such username found!");
            return;
        }
    }
}
