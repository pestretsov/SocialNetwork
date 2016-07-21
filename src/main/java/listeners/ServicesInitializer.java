package listeners;

import common.cp.ConnectionPool;
import common.cp.SimpleConnectionPool;
import dao.h2.H2PostDAO;
import dao.h2.H2UserDAO;
import dao.interfaces.PostDAO;
import dao.interfaces.UserDAO;
import utils.SecurityUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Created by artemypestretsov on 7/18/16.
 */

@WebListener
public class ServicesInitializer implements ServletContextListener {
    private static final String RESOURCES_FILE_PATH = "/WEB-INF/resources/";
    private static final String DB_PROPERTIES = "db.properties";
    private static final String DB_INIT_SCRIPT_FILE_NAME = "schema_init.sql";

    private static ConnectionPool connectionPool;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();

        String propertiesFilePath = servletContext.getRealPath(RESOURCES_FILE_PATH + DB_PROPERTIES);
        connectionPool = SimpleConnectionPool.create(propertiesFilePath);

        UserDAO userDAO = new H2UserDAO(connectionPool);
        PostDAO postDAO = new H2PostDAO(connectionPool);
        SecurityUtils securityUtils = new SecurityUtils();

        sce.getServletContext().setAttribute("userDAO", userDAO);
        sce.getServletContext().setAttribute("postDAO", postDAO);
        sce.getServletContext().setAttribute("securityUtils", securityUtils);

        String scriptFilePath = servletContext.getRealPath(RESOURCES_FILE_PATH + DB_INIT_SCRIPT_FILE_NAME);
        connectionPool.executeScript(scriptFilePath);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            connectionPool.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
