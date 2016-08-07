package dao.h2;

/**
 * Created by artemypestretsov on 8/7/16.
 */

import common.cp.ConnectionPool;
import common.cp.SimpleConnectionPool;
import dao.interfaces.PostDAO;
import dao.interfaces.PostViewDAO;
import dao.interfaces.UserDAO;
import model.Post;
import model.PostType;
import model.PostView;
import model.dbmodel.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.Assert.*;

public class H2PostViewDAOTest {
    private static final String RESOURCES_FILE_PATH = "src/test/resources/";
    private static final String DB_PROPERTIES = "db.properties";
    private static final String DB_PREPARE_FILE_NAME = "h2.sql";

    private static ConnectionPool connectionPool;
    private static PostViewDAO postViewDAO;
    private static PostDAO postDAO;
    private static UserDAO userDAO;

    @BeforeClass
    public static void connectionPoolTest() throws Exception {
        connectionPool = SimpleConnectionPool.create(RESOURCES_FILE_PATH + DB_PROPERTIES);
        connectionPool.executeScript(RESOURCES_FILE_PATH + DB_PREPARE_FILE_NAME);
        postViewDAO = new H2PostViewDAO(connectionPool);
        userDAO = new H2UserDAO(connectionPool);
        postDAO = new H2PostDAO(connectionPool);
    }

    @Test
    public void getSublistTest() throws Exception {
        Post post = new Post();
        post.setPostType(PostType.getPostTypeById(0));
        post.setPublishTime(Instant.now());
        post.setText("test test test text");
        post.setFromId(1);

        int postId1 = postDAO.create(post);

        post.setPostType(PostType.getPostTypeById(0));
        post.setPublishTime(Instant.now());
        post.setText("111111");
        post.setFromId(1);

        int postId2 = postDAO.create(post);

        List<PostView> sublist = postViewDAO.getSublist(1, 2, 1);

        assertEquals(sublist.size(), 1);
        assertEquals(sublist.get(0).getPostText(), "test test test text");

        postDAO.deleteById(postId1);
        postDAO.deleteById(postId2);
    }

    @AfterClass
    public static void closeAll() throws Exception {
        connectionPool.close();
    }
}
