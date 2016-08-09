package dao.h2;

import common.cp.ConnectionPool;
import common.cp.SimpleConnectionPool;
import dao.interfaces.CommentDAO;
import model.Comment;
import model.Post;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by artemypestretsov on 8/9/16.
 */
public class H2CommentDAOTest {
    private static final String RESOURCES_FILE_PATH = "src/test/resources/";
    private static final String DB_PROPERTIES = "db.properties";
    private static final String DB_PREPARE_FILE_NAME = "h2.sql";

    private static ConnectionPool connectionPool;
    private static H2PostDAO postDAO;
    private static H2CommentDAO commentDAO;

    @BeforeClass
    public static void connectionPoolTest() throws Exception {
        connectionPool = SimpleConnectionPool.create(RESOURCES_FILE_PATH + DB_PROPERTIES);
        connectionPool.executeScript(RESOURCES_FILE_PATH + DB_PREPARE_FILE_NAME);
        postDAO = new H2PostDAO(connectionPool);
        commentDAO = new H2CommentDAO(connectionPool);
    }

    @AfterClass
    public static void closeAll() throws Exception {
        connectionPool.close();
    }

    @Test
    public void addCommentTest() throws Exception {
        Post post = new Post();
        post.setFromId(1);

        post.setId(postDAO.create(post));

        Comment comment = new Comment();
        comment.setFromId(2);
        comment.setPostId(post.getId());

        comment.setId(commentDAO.addPostComment(comment));
        assertTrue(commentDAO.getById(1).getFromId() == 2);

        commentDAO.removePostComment(comment);
        postDAO.deleteById(post.getId());
    }
}
