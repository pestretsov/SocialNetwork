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
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.Instant;
import java.util.List;
import static org.hamcrest.CoreMatchers.is;

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
    public void getSublistBigTest() throws Exception {
        Post post1 = new Post();
        post1.setPostType(PostType.getPostTypeById(0));
        post1.setPublishTime(Instant.now());
        post1.setText("test test test text 111");
        post1.setFromId(1);

        Post post2 = new Post();
        post2.setPostType(PostType.getPostTypeById(0));
        post2.setPublishTime(Instant.now());
        post2.setText("test test test text 222");
        post2.setFromId(1);

        Post post3 = new Post();
        post3.setPostType(PostType.getPostTypeById(0));
        post3.setPublishTime(Instant.now());
        post3.setText("test test test text 333");
        post3.setFromId(1);

        Post post4 = new Post();
        post4.setPostType(PostType.getPostTypeById(0));
        post4.setPublishTime(Instant.now());
        post4.setText("test test test text 444");
        post4.setFromId(1);

        Post post5 = new Post();
        post5.setPostType(PostType.getPostTypeById(0));
        post5.setPublishTime(Instant.now());
        post5.setText("test test test text 555");
        post5.setFromId(1);

        int postId1 = postDAO.create(post1);
        int postId2 = postDAO.create(post2);
        int postId3 = postDAO.create(post3);
        int postId4 = postDAO.create(post4);
        int postId5 = postDAO.create(post5);

        List<PostView> postList = postViewDAO.getUserPostsSublist(1,1, 1000000, 10);

        assertTrue(postList.size() == 5);

        postList = postViewDAO.getUserPostsSublist(1, 1, 5, 3);
        assertTrue(postList.size() == 3);

        postList = postViewDAO.getUserPostsSublist(1,1, 4, 1000);
        assertTrue(postList.size() == 3);

        postDAO.deleteById(postId1);
        postDAO.deleteById(postId2);
        postDAO.deleteById(postId3);
        postDAO.deleteById(postId4);
        postDAO.deleteById(postId5);
    }


    @AfterClass
    public static void closeAll() throws Exception {
        connectionPool.close();
    }
}
