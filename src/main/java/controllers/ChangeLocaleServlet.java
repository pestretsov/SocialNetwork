package controllers;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * Created by artemypestretsov on 8/11/16.
 */

@Slf4j
@WebServlet(urlPatterns = "/changelocale")
public class ChangeLocaleServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String nextUrl = Optional.ofNullable(req.getParameter("next_url")).orElse("/");

        Optional.ofNullable(req.getParameter("locale"))
                .ifPresent(locale -> {
                    log.info("Setting locale to {}", locale);
                    req.getSession().setAttribute("locale", locale);
                });

        log.info("redirecting to {}", nextUrl);
        resp.sendRedirect(nextUrl);
    }
}
