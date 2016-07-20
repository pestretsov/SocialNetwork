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
import java.time.LocalDate;
import java.util.Optional;

/**
 * Created by artemypestretsov on 7/18/16.
 */

@WebServlet(urlPatterns = "/signup")
public class SignUpServlet extends HttpServlet {
    private UserDAO userDAO;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        userDAO = (UserDAO) getServletContext().getAttribute("userDAO");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("signup.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(true);

        User user = new User();

        String username = req.getParameter("j_username");
        String password = req.getParameter("j_password");
        String firstName = req.getParameter("first_name");
        String lastName = req.getParameter("last_name");

        if (userDAO.getByUsername(username).isPresent()) {
            resp.sendError(406, "This username is already in use");
        }

        String bio = Optional.ofNullable(req.getParameter("bio")).orElse("");
        String birthDate = Optional.ofNullable(req.getParameter("birth_date")).orElse("");

        user.setUsername(username);
        user.setPassword(password);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setBirthDate(LocalDate.parse(birthDate));
        user.setBio(bio);

        String nextURL = Optional.ofNullable((String) session.getAttribute("next")).orElse("/");

        try {
            userDAO.create(user);
            user = userDAO.getByUsername(username).orElseThrow(RuntimeException::new);

            session.setAttribute("user", user);
            resp.sendRedirect(nextURL);
        } catch (RuntimeException e) {
            resp.sendError(500, "Error creating user");
        }
    }
}
