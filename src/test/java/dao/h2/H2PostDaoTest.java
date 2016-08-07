package dao.h2;

import common.cp.ConnectionPool;
import common.cp.SimpleConnectionPool;
import dao.interfaces.PostDAO;
import model.dbmodel.PostEntity;
import model.dbmodel.PostTypeEntity;
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
        PostEntity post = new PostEntity();
        post.setPostType(PostTypeEntity.getPostTypeById(0));
        post.setPublishTime(Instant.now());
        post.setText("test test test text");
        post.setFromId(1);

        postDAO.create(post);
        PostEntity post1 = postDAO.getById(1).get();
        assertEquals(post1.getText(), post.getText());
        assertEquals(postDAO.getById(1).get().getText(), "test test test text");

        postDAO.deleteById(post.getId());
    }

    @Test
    public void getAllByFromIdTest() throws Exception {
        PostEntity post1 = new PostEntity();
        post1.setPostType(PostTypeEntity.getPostTypeById(0));
        post1.setPublishTime(Instant.now());
        post1.setText("test test test text");
        post1.setFromId(1);

        PostEntity post2 = new PostEntity();
        post2.setPostType(PostTypeEntity.getPostTypeById(1));
        post2.setPublishTime(Instant.now());
        post2.setText("123_123");
        post2.setFromId(1);

        PostEntity post3 = new PostEntity();
        post3.setPostType(PostTypeEntity.getPostTypeById(1));
        post3.setPublishTime(Instant.now());
        post3.setText("mmm nice");
        post3.setFromId(2);

        postDAO.create(post1);
        postDAO.create(post2);
        postDAO.create(post3);

        List<PostEntity> posts = postDAO.getAllByFromId(1);
        assertEquals(posts.size(), 2);
        for (PostEntity post: posts) {
            assertEquals(post.getFromId(), 1);
        }

        postDAO.deleteById(post1.getId());
        postDAO.deleteById(post2.getId());
        postDAO.deleteById(post3.getId());
    }

    @Test
    public void deletePostByIdTest() throws Exception {
        PostEntity post1 = new PostEntity();
        post1.setPostType(PostTypeEntity.getPostTypeById(1));
        post1.setPublishTime(Instant.now());
        post1.setText("test test test text");
        post1.setFromId(1);

        PostEntity post2 = new PostEntity();
        post2.setPostType(PostTypeEntity.getPostTypeById(1));
        post2.setPublishTime(Instant.now());
        post2.setText("123_123");
        post2.setFromId(1);

        PostEntity post3 = new PostEntity();
        post3.setPostType(PostTypeEntity.getPostTypeById(1));
        post3.setPublishTime(Instant.now());
        post3.setText("mmm nice");
        post3.setFromId(2);

        postDAO.create(post1);
        postDAO.create(post2);
        postDAO.create(post3);

        assertTrue(postDAO.deleteById(1));
        assertFalse(postDAO.getById(1).isPresent());

        postDAO.deleteById(post1.getId());
        postDAO.deleteById(post2.getId());
        postDAO.deleteById(post3.getId());
    }

    @Test
    public void updatePostTest() throws Exception {
        PostEntity post1 = new PostEntity();
        post1.setPostType(PostTypeEntity.getPostTypeById(1));
        post1.setPublishTime(Instant.now());
        post1.setText("test test test text");
        post1.setFromId(1);

        postDAO.create(post1);
        assertTrue(postDAO.getById(1).isPresent());
        PostEntity post2 = postDAO.getById(1).get();
        post2.setText("updated text");
        postDAO.update(post2);
        assertEquals(postDAO.getById(1).get().getText(), "updated text");
        assertEquals(postDAO.getById(1).get().getFromId(), post1.getFromId());

        postDAO.deleteById(post2.getId());
    }

    @Test
    public void getSublistTest() throws Exception {
        PostEntity post1 = new PostEntity();
        post1.setPostType(PostTypeEntity.getPostTypeById(0));
        post1.setPublishTime(Instant.now());
        post1.setText("test test test text 111");
        post1.setFromId(1);

        PostEntity post2 = new PostEntity();
        post2.setPostType(PostTypeEntity.getPostTypeById(0));
        post2.setPublishTime(Instant.now());
        post2.setText("test test test text 222");
        post2.setFromId(1);

        PostEntity post3 = new PostEntity();
        post3.setPostType(PostTypeEntity.getPostTypeById(0));
        post3.setPublishTime(Instant.now());
        post3.setText("test test test text 333");
        post3.setFromId(1);

        PostEntity post4 = new PostEntity();
        post4.setPostType(PostTypeEntity.getPostTypeById(0));
        post4.setPublishTime(Instant.now());
        post4.setText("test test test text 444");
        post4.setFromId(1);

        PostEntity post5 = new PostEntity();
        post5.setPostType(PostTypeEntity.getPostTypeById(0));
        post5.setPublishTime(Instant.now());
        post5.setText("test test test text 555");
        post5.setFromId(1);

        postDAO.create(post1);
        postDAO.create(post2);
        postDAO.create(post3);
        postDAO.create(post4);
        postDAO.create(post5);

        List<PostEntity> postList = postDAO.getSublistByFromId(1, 0, 100);

        assertTrue(postList.size() == 5);

        postList = postDAO.getSublistByFromId(1, 2, 5);
        assertTrue(postList.size() == 3);

        postList = postDAO.getSublistByFromId(1, 2, 4);
        assertTrue(postList.size() == 3);

        postDAO.deleteById(post1.getId());
        postDAO.deleteById(post2.getId());
        postDAO.deleteById(post3.getId());
        postDAO.deleteById(post4.getId());
        postDAO.deleteById(post5.getId());
    }

    @AfterClass
    public static void closeAll() throws Exception {
        connectionPool.close();
    }
}
