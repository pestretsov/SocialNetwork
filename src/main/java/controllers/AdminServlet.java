package controllers;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by artemypestretsov on 8/11/16.
 */
@Slf4j
@WebServlet(urlPatterns = "/admin/adminpage")
public class AdminServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setAttribute("users", userDAO.getAllUsers());

        req.getRequestDispatcher("/WEB-INF/admin.jsp").forward(req, resp);
    }
}
