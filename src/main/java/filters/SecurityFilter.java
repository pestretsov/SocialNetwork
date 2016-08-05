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

@WebFilter(urlPatterns = {"/secure", "/createpost"})
public class SecurityFilter implements Filter {
    private UserDAO userDAO;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        userDAO = (UserDAO) filterConfig.getServletContext().getAttribute("userDAO");
    }

    // TODO: узнать у Романа, как этого избежать
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String uri = ((HttpServletRequest) request).getRequestURI();

        // to ensure, that no crazy things are displayed in browser
//        request.setCharacterEncoding("UTF-8");

//        if (uri.contains("/css")) {
//            chain.doFilter(request, response);
//        } else if (uri.contains("/images")) {
//            chain.doFilter(request, response);
//        } else if (uri.contains("/js")) {
//            chain.doFilter(request, response);
//        } else if (uri.contains("/fonts")) {
//            chain.doFilter(request, response);
//        } else if (uri.contains("/favicon")) {
//            chain.doFilter(request, response);
//        } else if (uri.contains("/restapi")) {
//            chain.doFilter(request, response);
//        } else {
            doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
//        }
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpSession session = request.getSession(true);
        String fromUri = request.getRequestURI();

        System.out.println("Security filter. FromUri=" + fromUri);

//        if (fromUri.equals("/signup") || fromUri.equals("/login")) {
//            chain.doFilter(request, response);
//        } else {
            Optional<User> userOpt =
                    Optional.ofNullable((User)session.getAttribute("sessionUser"))
                            .map(User::getId)
                            .flatMap(userDAO::getById);

            if (userOpt.isPresent()) {
                session.setAttribute("sessionUser", userOpt.get());
                chain.doFilter(request, response);
            } else {
                session.removeAttribute("sessionUser");
                session.setAttribute("next", fromUri);
                request.getRequestDispatcher("/login").forward(request, response);
            }
//        }
    }

    @Override
    public void destroy() {

    }
}
