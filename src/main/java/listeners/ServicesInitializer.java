package listeners;

import common.cp.ConnectionPool;
import common.cp.SimpleConnectionPool;
import dao.h2.*;
import dao.interfaces.*;
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

    public static final String USER_DAO = "userDAO";
    public static final String POST_DAO = "postDAO";
    public static final String POST_VIEW_DAO = "postViewDAO";
    public static final String FOLLOW_DAO = "followDAO";
    public static final String LIKE_DAO = "likeDAO";
    public static final String COMMENT_DAO = "commentDAO";
    public static final String COMMENT_VIEW_DAO = "commentViewDAO";
    public static final String SECURITY_UTILS = "securityUtils";

    private static ConnectionPool connectionPool;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();

        String propertiesFilePath = servletContext.getRealPath(RESOURCES_FILE_PATH + DB_PROPERTIES);
        connectionPool = SimpleConnectionPool.create(propertiesFilePath);

        UserDAO userDAO = new H2UserDAO(connectionPool);
        PostDAO postDAO = new H2PostDAO(connectionPool);
        PostViewDAO postViewDAO = new H2PostViewDAO(connectionPool);
        FollowDAO followDAO = new H2FollowDAO(connectionPool);
        LikeDAO likeDAO = new H2LikeDAO(connectionPool);
        CommentDAO commentDAO = new H2CommentDAO(connectionPool);
        CommentViewDAO commentViewDAO = new H2CommentViewDAO(connectionPool);
        SecurityUtils securityUtils = new SecurityUtils();

        sce.getServletContext().setAttribute(USER_DAO, userDAO);
        sce.getServletContext().setAttribute(POST_DAO, postDAO);
        sce.getServletContext().setAttribute(POST_VIEW_DAO, postViewDAO);
        sce.getServletContext().setAttribute(FOLLOW_DAO, followDAO);
        sce.getServletContext().setAttribute(LIKE_DAO, likeDAO);
        sce.getServletContext().setAttribute(COMMENT_DAO, commentDAO);
        sce.getServletContext().setAttribute(COMMENT_VIEW_DAO, commentViewDAO);
        sce.getServletContext().setAttribute(SECURITY_UTILS, securityUtils);

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
