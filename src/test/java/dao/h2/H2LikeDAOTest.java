package dao.h2;

import common.cp.ConnectionPool;
import common.cp.SimpleConnectionPool;
import model.Post;
import model.User;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by artemypestretsov on 8/8/16.
 */
public class H2LikeDAOTest {
    private static final String RESOURCES_FILE_PATH = "src/test/resources/";
    private static final String DB_PROPERTIES = "db.properties";
    private static final String DB_PREPARE_FILE_NAME = "h2.sql";

    private static ConnectionPool connectionPool;
    private static H2PostDAO postDAO;
    private static H2UserDAO userDAO;
    private static H2LikeDAO likeDAO;
    private static H2PostViewDAO postViewDAO;

    @BeforeClass
    public static void connectionPoolTest() throws Exception {
        connectionPool = SimpleConnectionPool.create(RESOURCES_FILE_PATH + DB_PROPERTIES);
        connectionPool.executeScript(RESOURCES_FILE_PATH + DB_PREPARE_FILE_NAME);
        postDAO = new H2PostDAO(connectionPool);
        userDAO = new H2UserDAO(connectionPool);
        likeDAO = new H2LikeDAO(connectionPool);
        postViewDAO = new H2PostViewDAO(connectionPool);
    }

    @AfterClass
    public static void closeAll() throws Exception {
        connectionPool.close();
    }

    @Test
    public void addAndRemoveLikeTest() throws Exception {
        Post post = new Post();
        post.setFromId(1);
        post.setId(postDAO.create(post));

        User user = userDAO.getById(1).get();

        assertTrue(likeDAO.addLike(post.getId(), user.getId()) >= 0);
        assertTrue(likeDAO.hasLike(post.getId(), user.getId()));

        assertTrue(likeDAO.removeLike(post.getId(), user.getId()));
        assertFalse(likeDAO.hasLike(post.getId(), user.getId()));

        postDAO.deleteById(post.getId());
    }

    @Test
    public void canLikeTest() throws Exception {
        Post post = new Post();
        post.setFromId(1);
        post.setId(postDAO.create(post));

        User user = userDAO.getById(2).get();

        assertTrue(likeDAO.addLike(post.getId(), user.getId()) >= 0);
        assertTrue(likeDAO.hasLike(post.getId(), user.getId()));

        assertFalse(postViewDAO.getUserPostsSublist(2, 1, 1000, 3).get(0).isLikable());

        postDAO.deleteById(post.getId());
    }
}
