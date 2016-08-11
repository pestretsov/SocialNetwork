package filters;

import lombok.extern.slf4j.Slf4j;
import model.User;
import model.UserRole;
import utils.SecurityUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * Created by artemypestretsov on 8/11/16.
 */
@Slf4j
@WebFilter(urlPatterns = "/admin/*")
public class AdminFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        Optional<User> admin = SecurityUtils.getSessionUserOpt(request.getSession(false));

        if (!admin.isPresent() || admin.get().getRole() != UserRole.ADMIN) {
            log.warn("user is not administrator");
            response.sendError(500, "you dont have permission");
            return;
        }

        log.info("user is administrator");
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
