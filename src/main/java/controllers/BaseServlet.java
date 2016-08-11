package controllers;

import dao.interfaces.*;
import listeners.ServicesInitializer;
import utils.SecurityUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * Created by artemypestretsov on 8/9/16.
 */
public class BaseServlet extends HttpServlet {

    protected static final String SESSION_USER = "sessionUser";

    protected UserDAO userDAO;
    protected PostDAO postDAO;
    protected PostViewDAO postViewDAO;
    protected FollowDAO followDAO;
    protected LikeDAO likeDAO;
    protected SecurityUtils securityUtils;

    @Override
    public void init() throws ServletException {
        userDAO       = (UserDAO)       getServletContext().getAttribute(ServicesInitializer.USER_DAO);
        postDAO       = (PostDAO)       getServletContext().getAttribute(ServicesInitializer.POST_DAO);
        postViewDAO   = (PostViewDAO)   getServletContext().getAttribute(ServicesInitializer.POST_VIEW_DAO);
        followDAO     = (FollowDAO)     getServletContext().getAttribute(ServicesInitializer.FOLLOW_DAO);
        likeDAO       = (LikeDAO)       getServletContext().getAttribute(ServicesInitializer.LIKE_DAO);
        securityUtils = (SecurityUtils) getServletContext().getAttribute(ServicesInitializer.SECURITY_UTILS);
    }
}
