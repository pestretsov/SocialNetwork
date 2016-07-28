package controllers;

import dao.interfaces.UserDAO;
import model.User;
import utils.SecurityUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

/**
 * Created by artemypestretsov on 7/18/16.
 */

@WebServlet(urlPatterns = "/login")
public class LogInServlet extends HttpServlet {
    private UserDAO userDAO;
    private SecurityUtils securityUtils;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        userDAO = (UserDAO) getServletContext().getAttribute("userDAO");
        securityUtils = (SecurityUtils) getServletContext().getAttribute("securityUtils");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        String username = req.getParameter("j_username");
        String password = req.getParameter("j_password");
        String nextURL = Optional.ofNullable((String) session.getAttribute("next")).orElse("/");

        Optional<User> userOpt = userDAO.getByUsername(username);

        if (!userOpt.isPresent()) {
            resp.sendError(406, "This username is not found");
            return;
        }

        User user = userOpt.get();

        if (securityUtils.validatePassword(password, user.getPassword())) {
            session.setAttribute("user", user);
            resp.sendRedirect(nextURL);
        } else {
            resp.sendError(406, "Wrong password");
        }
    }
}
