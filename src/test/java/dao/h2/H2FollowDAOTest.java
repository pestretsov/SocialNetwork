package dao.h2;

import common.cp.ConnectionPool;
import common.cp.SimpleConnectionPool;
import model.Follow;
import model.User;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

/**
 * Created by artemypestretsov on 7/24/16.
 */
public class H2FollowDAOTest {
    private static final String RESOURCES_FILE_PATH = "src/test/resources/";
    private static final String DB_PROPERTIES = "db.properties";
    private static final String DB_PREPARE_FILE_NAME = "h2.sql";

    private static ConnectionPool connectionPool;
    private static H2FollowDAO followDAO;
    private static H2UserDAO userDAO;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @BeforeClass
    public static void connectionPoolTest() throws Exception {
        connectionPool = SimpleConnectionPool.create(RESOURCES_FILE_PATH + DB_PROPERTIES);
        connectionPool.executeScript(RESOURCES_FILE_PATH + DB_PREPARE_FILE_NAME);
        followDAO = new H2FollowDAO(connectionPool);
        userDAO = new H2UserDAO(connectionPool);
    }

    @Test
    public void createFollowTest() throws Exception {
        Follow follow = new Follow();
        follow.setFollowerId(1);
        follow.setUserId(2);

        followDAO.create(follow);

        assertEquals(userDAO.getUsersFollowedByUser(1).size(), 1);

        followDAO.delete(follow);
    }

    @Test
    public void followRelationTest() throws Exception {
        Follow follow1 = new Follow();
        follow1.setFollowerId(1);
        follow1.setUserId(2);

        Follow follow2 = new Follow();
        follow2.setFollowerId(1);
        follow2.setUserId(3);

        Follow follow3 = new Follow();
        follow3.setFollowerId(1);
        follow3.setUserId(4);

        followDAO.create(follow1);
        followDAO.create(follow2);
        followDAO.create(follow3);

        assertEquals(userDAO.getUsersFollowingUser(2).size(), 1);
        assertEquals(userDAO.getUsersFollowingUser(3).size(), 1);
        assertEquals(userDAO.getUsersFollowingUser(4).size(), 1);
        assertEquals(userDAO.getUsersFollowedByUser(1).size(), 3);

        for (User user: userDAO.getUsersFollowingUser(2)) {
            assertEquals(user.getUsername(), "ambush");
        }

        for (User user: userDAO.getUsersFollowingUser(3)) {
            assertEquals(user.getUsername(), "ambush");
        }

        followDAO.delete(follow1);
        followDAO.delete(follow2);
        followDAO.delete(follow3);
    }

    @Test
    public void followYourselfTest() throws Exception {
        Follow follow = new Follow();
        follow.setFollowerId(1);
        follow.setUserId(1);

        followDAO.create(follow);

        followDAO.getByUserAndFollowerId(1, 1);

        for (User user: userDAO.getUsersFollowingUser(1)) {
            assertEquals(user.getUsername(), "ambush");
        }

        for (User user: userDAO.getUsersFollowedByUser(1)) {
            assertEquals(user.getUsername(), "ambush");
        }

        followDAO.delete(follow);
    }

    @Test
    public void countersTest() throws Exception {
        Follow follow1 = new Follow();
        follow1.setUserId(1);
        follow1.setFollowerId(2);

        followDAO.create(follow1);

        Follow follow2 = new Follow();
        follow2.setUserId(1);
        follow2.setFollowerId(3);

        followDAO.create(follow2);

        assertEquals(followDAO.getAllFollowersByUser(1), 2);

        followDAO.delete(follow1);
        followDAO.delete(follow2);
    }

    @AfterClass
    public static void closeAll() throws Exception {
        connectionPool.close();
    }
}
