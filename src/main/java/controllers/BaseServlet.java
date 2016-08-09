package controllers;

import dao.interfaces.FollowDAO;
import dao.interfaces.LikeDAO;
import dao.interfaces.PostDAO;
import dao.interfaces.UserDAO;
import utils.SecurityUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * Created by artemypestretsov on 8/9/16.
 */
public class BaseServlet extends HttpServlet {

    protected UserDAO userDAO;
    protected PostDAO postDAO;
    protected FollowDAO followDAO;
    protected LikeDAO likeDAO;
    protected SecurityUtils securityUtils;

    @Override
    public void init() throws ServletException {
        userDAO       = (UserDAO)       getServletContext().getAttribute("userDAO");
        postDAO       = (PostDAO)       getServletContext().getAttribute("postDAO");
        followDAO     = (FollowDAO)     getServletContext().getAttribute("followDAO");
        likeDAO       = (LikeDAO)       getServletContext().getAttribute("likeDAO");
        securityUtils = (SecurityUtils) getServletContext().getAttribute("securityUtils");
    }
}
