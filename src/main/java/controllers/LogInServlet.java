package controllers;

import dao.interfaces.UserDAO;
import model.User;

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

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        userDAO = (UserDAO) getServletContext().getAttribute("userDAO");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        String username = req.getParameter("j_username");
        String password = req.getParameter("j_password");

        Optional<User> user = userDAO.getByUsername(username);

        if (!user.isPresent()) {
            resp.sendError(406, "This username is not found");
            System.out.println("WRONG USERNAME");
            return;
        }

        if (user.get().getPassword().equals(password)) {
            session.setAttribute("user", user.get());
            System.out.println("PASSWORD");
            resp.sendRedirect("/");
        } else {
            System.out.println("WRONG PASSWORD");
            resp.sendError(406, "Wrong password");
        }
    }
}
