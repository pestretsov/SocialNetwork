package dao.h2;

import common.cp.ConnectionPool;
import common.cp.SimpleConnectionPool;
import dao.interfaces.PostDAO;
import dao.interfaces.PostViewDAO;
import model.Post;
import model.PostType;
import model.PostView;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by artemypestretsov on 7/13/16.
 */
public class H2PostDAOTest {
    private static final String RESOURCES_FILE_PATH = "src/test/resources/";
    private static final String DB_PROPERTIES = "db.properties";
    private static final String DB_PREPARE_FILE_NAME = "h2.sql";

    private static ConnectionPool connectionPool;
    private static PostDAO postDAO;

    @BeforeClass
    public static void connectionPoolTest() throws Exception {
        connectionPool = SimpleConnectionPool.create(RESOURCES_FILE_PATH + DB_PROPERTIES);
        connectionPool.executeScript(RESOURCES_FILE_PATH + DB_PREPARE_FILE_NAME);
        postDAO = new H2PostDAO(connectionPool);
    }
    @Before
    public void executeQueryOnConnectionPoolTest() throws Exception {
        connectionPool.executeScript(RESOURCES_FILE_PATH + DB_PREPARE_FILE_NAME);
    }

    @Test
    public void createAndGetByIdPostTest() throws Exception {
        Post post = new Post();
        post.setPostType(PostType.getPostTypeById(0));
        post.setPublishTime(Instant.now());
        post.setText("test test test text");
        post.setFromId(1);

        postDAO.create(post);
        Post post1 = postDAO.getById(1).get();
        assertEquals(post1.getText(), post.getText());
        assertEquals(postDAO.getById(1).get().getText(), "test test test text");

        postDAO.deleteById(post1.getId());
    }

    @Test
    public void getAllByFromIdTest() throws Exception {
        Post post1 = new Post();
        post1.setPostType(PostType.getPostTypeById(0));
        post1.setPublishTime(Instant.now());
        post1.setText("test test test text");
        post1.setFromId(1);

        Post post2 = new Post();
        post2.setPostType(PostType.getPostTypeById(1));
        post2.setPublishTime(Instant.now());
        post2.setText("123_123");
        post2.setFromId(1);

        Post post3 = new Post();
        post3.setPostType(PostType.getPostTypeById(1));
        post3.setPublishTime(Instant.now());
        post3.setText("mmm nice");
        post3.setFromId(2);

        int postId1 = postDAO.create(post1);
        int postId2 = postDAO.create(post2);
        int postId3 = postDAO.create(post3);

        List<Post> posts = postDAO.getAllByFromId(1);
        assertEquals(posts.size(), 2);
        for (Post post: posts) {
            assertEquals(post.getFromId(), 1);
        }

        postDAO.deleteById(postId1);
        postDAO.deleteById(postId2);
        postDAO.deleteById(postId3);
    }

    @Test
    public void deletePostByIdTest() throws Exception {
        Post post1 = new Post();
        post1.setPostType(PostType.getPostTypeById(1));
        post1.setPublishTime(Instant.now());
        post1.setText("test test test text");
        post1.setFromId(1);

        Post post2 = new Post();
        post2.setPostType(PostType.getPostTypeById(1));
        post2.setPublishTime(Instant.now());
        post2.setText("123_123");
        post2.setFromId(1);

        Post post3 = new Post();
        post3.setPostType(PostType.getPostTypeById(1));
        post3.setPublishTime(Instant.now());
        post3.setText("mmm nice");
        post3.setFromId(2);

        int postId1 = postDAO.create(post1);
        int postId2 = postDAO.create(post2);
        int postId3 = postDAO.create(post3);

        assertTrue(postDAO.deleteById(1));
        assertFalse(postDAO.getById(1).isPresent());

        postDAO.deleteById(postId1);
        postDAO.deleteById(postId2);
        postDAO.deleteById(postId3);
    }

    @Test
    public void updatePostTest() throws Exception {
        Post post1 = new Post();
        post1.setPostType(PostType.getPostTypeById(1));
        post1.setPublishTime(Instant.now());
        post1.setText("test test test text");
        post1.setFromId(1);

        postDAO.create(post1);
        assertTrue(postDAO.getById(1).isPresent());
        Post post2 = postDAO.getById(1).get();
        post2.setText("updated text");
        postDAO.update(post2);
        assertEquals(postDAO.getById(1).get().getText(), "updated text");
        assertEquals(postDAO.getById(1).get().getFromId(), post1.getFromId());

        postDAO.deleteById(post2.getId());
    }

    @AfterClass
    public static void closeAll() throws Exception {
        connectionPool.close();
    }
}
