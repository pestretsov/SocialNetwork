package filters;

import dao.interfaces.UserDAO;
import model.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

/**
 * Created by artemypestretsov on 7/18/16.
 */

@WebFilter(urlPatterns = "/*")
public class SecurityFilter implements Filter {
    private UserDAO userDAO;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        userDAO = (UserDAO) filterConfig.getServletContext().getAttribute("userDAO");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpSession session = request.getSession(true);
        String fromUri = request.getRequestURI();

        Optional<User> oo = userDAO.getByUsername("ambush");
        if (oo.isPresent()) {
            System.out.println(oo.get().getPassword());
        } else {
            System.out.println("WTF");
        }

        System.out.println(fromUri);
        if (fromUri.equals("/signup") || fromUri.equals("/login")) {
            System.out.println("HERE1");
            chain.doFilter(request, response);
        } else {
            System.out.println("HERE2");
            Optional<User> userOpt =
                    Optional.ofNullable((User)session.getAttribute("user"))
                            .map(user -> user.getId())
                            .flatMap(userId -> userDAO.getById(userId));

            if (userOpt.isPresent()) {
                session.setAttribute("user", userOpt.get());
                System.out.println("HERE3");
                chain.doFilter(request, response);
            } else {
                session.removeAttribute("user");
                System.out.println("HERE4");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            }
        }
    }

    @Override
    public void destroy() {

    }
}
