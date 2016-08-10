package controllers;

import dao.interfaces.*;
import utils.SecurityUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * Created by artemypestretsov on 8/9/16.
 */
public class BaseServlet extends HttpServlet {

    protected UserDAO userDAO;
    protected PostDAO postDAO;
    protected PostViewDAO postViewDAO;
    protected FollowDAO followDAO;
    protected LikeDAO likeDAO;
    protected SecurityUtils securityUtils;

    @Override
    public void init() throws ServletException {
        userDAO       = (UserDAO)       getServletContext().getAttribute("userDAO");
        postDAO       = (PostDAO)       getServletContext().getAttribute("postDAO");
        postViewDAO   = (PostViewDAO)   getServletContext().getAttribute("postViewDAO");
        followDAO     = (FollowDAO)     getServletContext().getAttribute("followDAO");
        likeDAO       = (LikeDAO)       getServletContext().getAttribute("likeDAO");
        securityUtils = (SecurityUtils) getServletContext().getAttribute("securityUtils");
    }
}
