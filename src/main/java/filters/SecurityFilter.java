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

    // TODO: узнать у Романа, как этого избежать
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String uri = ((HttpServletRequest)request).getRequestURI();
        if (uri.startsWith("/css")) {
            chain.doFilter(request, response);
        } else if (uri.startsWith("/images")) {
            chain.doFilter(request, response);
        } else if (uri.startsWith("/js")) {
            chain.doFilter(request, response);
        } else if (uri.startsWith("/fonts")) {
            chain.doFilter(request, response);
        } else {
            doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
        }
    }

    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpSession session = request.getSession(true);
        String fromUri = request.getRequestURI();

        System.out.println("Security filter. FromUri=" + fromUri);

        if (fromUri.equals("/signup") || fromUri.equals("/login")) {
            chain.doFilter(request, response);
        } else {
            Optional<User> userOpt =
                    Optional.ofNullable((User)session.getAttribute("user"))
                            .map(user -> user.getId())
                            .flatMap(userId -> userDAO.getById(userId));

            if (userOpt.isPresent()) {
                session.setAttribute("user", userOpt.get());
                chain.doFilter(request, response);
            } else {
                session.removeAttribute("user");
                request.getRequestDispatcher("/login").forward(request, response);
            }
        }
    }

    @Override
    public void destroy() {

    }
}
