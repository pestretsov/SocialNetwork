package filters;

import dao.interfaces.UserDAO;
import lombok.extern.slf4j.Slf4j;
import model.dbmodel.UserEntity;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

/**
 * Created by artemypestretsov on 7/18/16.
 */

@Slf4j
@WebFilter(urlPatterns = {"/secure/*"})
public class SecurityFilter implements Filter {
    private UserDAO userDAO;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        userDAO = (UserDAO) filterConfig.getServletContext().getAttribute("userDAO");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String uri = ((HttpServletRequest) request).getRequestURI();

        log.info("filtering requestUri={}", uri);

        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpSession session = request.getSession(true);
        String fromUri = request.getRequestURI();


        Optional<UserEntity> userOpt =
                Optional.ofNullable((UserEntity)session.getAttribute("sessionUser"))
                        .map(UserEntity::getId)
                        .flatMap(userDAO::getById);

        if (userOpt.isPresent()) {
            log.info("user logged id={}", userOpt.get().getId());
            session.setAttribute("sessionUser", userOpt.get());
            chain.doFilter(request, response);
        } else {
            log.info("SessionUser not found. Redirect to /login page.");
            session.removeAttribute("sessionUser");
            session.setAttribute("next", fromUri);
            request.getRequestDispatcher("/login").forward(request, response);
        }
    }

    @Override
    public void destroy() {

    }
}
