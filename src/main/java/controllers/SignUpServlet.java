package controllers;

import dao.interfaces.FollowDAO;
import dao.interfaces.UserDAO;
import lombok.extern.slf4j.Slf4j;
import model.dbmodel.FollowEntity;
import model.dbmodel.UserEntity;
import utils.SecurityUtils;
import utils.Validator;

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

@Slf4j
@WebServlet(urlPatterns = "/signup")
public class SignUpServlet extends HttpServlet {
    private UserDAO userDAO;
    private FollowDAO followDAO;
    private SecurityUtils securityUtils;

    @Override
    public void init() throws ServletException {
        userDAO = (UserDAO) getServletContext().getAttribute("userDAO");
        followDAO = (FollowDAO) getServletContext().getAttribute("followDAO");
        securityUtils = (SecurityUtils) getServletContext().getAttribute("securityUtils");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/signup.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(true);

        log.info("attempt to sign up");

        UserEntity user = new UserEntity();

        String username = req.getParameter("j_username");
        String password = req.getParameter("j_password");
        String firstName = req.getParameter("first_name");
        String lastName = req.getParameter("last_name");

        if (!Validator.validateUsername(username)) {
            log.warn("illegal username={}", username);
            resp.sendError(406, "Invalid character in username");
            return;
        }

        if (userDAO.getByUsername(username).isPresent()) {
            log.warn("username={} is already in use", username);
            resp.sendError(406, "This username is already in use");
            return;
        }

        String bio = Optional.ofNullable(req.getParameter("bio")).orElse("");
        String birthDate = Optional.ofNullable(req.getParameter("birth_date")).orElse("");

        String passwordHash = securityUtils.encrypt(password);

        int sexOpt = Optional.ofNullable(req.getParameter("sex")).map(Integer::parseInt).orElse(0);

        user.setUsername(username);
        user.setPassword(passwordHash);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setBirthDate(LocalDate.parse(birthDate));
        user.setSex(sexOpt);
        user.setBio(bio);

        String nextURL = Optional.ofNullable((String) session.getAttribute("next")).orElse("/");

        try {
            userDAO.create(user);
            user = userDAO.getByUsername(username).orElseThrow(RuntimeException::new);

            FollowEntity follow = new FollowEntity();
            follow.setUserId(user.getId());
            follow.setFollowerId(user.getId());

            followDAO.create(follow);

            session.setAttribute("sessionUser", user);
            resp.sendRedirect(nextURL);
        } catch (RuntimeException e) {
            log.warn("error creating user");
            resp.sendError(500, "Error creating user");
            return;
        }

        log.info("user with userId={} was successfully signed up", user.getId());
    }
}
