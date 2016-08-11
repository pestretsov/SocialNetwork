package dao.h2;

import common.cp.ConnectionPool;
import common.cp.SimpleConnectionPool;
import dao.interfaces.*;
import model.Comment;
import model.CommentView;
import model.Post;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by artemypestretsov on 8/10/16.
 */
public class H2CommentViewDAOTest {
    private static final String RESOURCES_FILE_PATH = "src/test/resources/";
    private static final String DB_PROPERTIES = "db.properties";
    private static final String DB_PREPARE_FILE_NAME = "h2.sql";

    private static ConnectionPool connectionPool;
    private static CommentViewDAO commentViewDAO;
    private static PostDAO postDAO;
    private static CommentDAO commentDAO;

    @BeforeClass
    public static void connectionPoolTest() throws Exception {
        connectionPool = SimpleConnectionPool.create(RESOURCES_FILE_PATH + DB_PROPERTIES);
        connectionPool.executeScript(RESOURCES_FILE_PATH + DB_PREPARE_FILE_NAME);
        commentViewDAO = new H2CommentViewDAO(connectionPool);
        commentDAO = new H2CommentDAO(connectionPool);
        postDAO = new H2PostDAO(connectionPool);
    }

    @Test
    public void getSublistTest() throws Exception {
        Post post = new Post();
        post.setFromId(1);
        post.setText("test");

        post.setId(postDAO.create(post));

        Comment comment1 = new Comment();
        comment1.setPostId(post.getId());
        comment1.setFromId(1);

        commentDAO.addPostComment(comment1);

        Comment comment2 = new Comment();
        comment2.setPostId(post.getId());
        comment2.setFromId(2);

        commentDAO.addPostComment(comment2);

        Comment comment3 = new Comment();
        comment3.setPostId(post.getId());
        comment3.setFromId(3);

        commentDAO.addPostComment(comment3);

        assertEquals(commentViewDAO.getPostCommentsSublist(post.getId(), 4, 3).size(), 3);
        assertEquals(commentViewDAO.getPostCommentsSublist(post.getId(), 1, 3).size(), 0);
        assertEquals(commentViewDAO.getPostCommentsSublist(post.getId(), 3, 3).size(), 2);

//        for (CommentView commentView : commentViewDAO.getPostCommentsSublist(post.getId(), 0, 3)) {
//            System.out.println(commentView.getFromUsername());
//        }

        postDAO.deleteById(post.getId());
    }

    @AfterClass
    public static void closeAll() throws Exception {
        connectionPool.close();
    }
}
