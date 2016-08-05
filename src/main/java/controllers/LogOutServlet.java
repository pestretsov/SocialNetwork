package controllers;

import dao.interfaces.UserDAO;
import lombok.extern.slf4j.Slf4j;
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

/**
 * Created by artemypestretsov on 7/22/16.
 */

@Slf4j
@WebServlet(urlPatterns = "/logout")
public class LogOutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        User sessionUser = (User) req.getSession().getAttribute("sessionUser");
        if (sessionUser != null) {
            log.info("logout userId={}. Redirecting to {}", sessionUser.getId(), "/");
        } else {
            log.info("no session. Redirection to {}", "/");
        }

        session.removeAttribute("sessionUser");
        session.invalidate();

        resp.sendRedirect("/");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
