package dao.h2;

import common.cp.ConnectionPool;
import common.cp.SimpleConnectionPool;
import model.dbmodel.FollowEntity;
import model.dbmodel.UserEntity;
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
        FollowEntity follow = new FollowEntity();
        follow.setFollowerId(1);
        follow.setUserId(2);

        followDAO.create(follow);

        assertEquals(userDAO.getUsersFollowedByUser(1).size(), 1);

        followDAO.delete(follow);
    }

    @Test
    public void followRelationTest() throws Exception {
        FollowEntity follow1 = new FollowEntity();
        follow1.setFollowerId(1);
        follow1.setUserId(2);

        FollowEntity follow2 = new FollowEntity();
        follow2.setFollowerId(1);
        follow2.setUserId(3);

        FollowEntity follow3 = new FollowEntity();
        follow3.setFollowerId(1);
        follow3.setUserId(4);

        followDAO.create(follow1);
        followDAO.create(follow2);
        followDAO.create(follow3);

        assertEquals(userDAO.getUsersFollowingUser(2).size(), 1);
        assertEquals(userDAO.getUsersFollowingUser(3).size(), 1);
        assertEquals(userDAO.getUsersFollowingUser(4).size(), 1);
        assertEquals(userDAO.getUsersFollowedByUser(1).size(), 3);

        for (UserEntity user: userDAO.getUsersFollowingUser(2)) {
            assertEquals(user.getUsername(), "ambush");
        }

        for (UserEntity user: userDAO.getUsersFollowingUser(3)) {
            assertEquals(user.getUsername(), "ambush");
        }

        followDAO.delete(follow1);
        followDAO.delete(follow2);
        followDAO.delete(follow3);
    }

    @Test
    public void followYourselfTest() throws Exception {
        FollowEntity follow = new FollowEntity();
        follow.setFollowerId(1);
        follow.setUserId(1);

        followDAO.create(follow);

        followDAO.getByUserAndFollowerId(1, 1);

        for (UserEntity user: userDAO.getUsersFollowingUser(1)) {
            assertEquals(user.getUsername(), "ambush");
        }

        for (UserEntity user: userDAO.getUsersFollowedByUser(1)) {
            assertEquals(user.getUsername(), "ambush");
        }

        followDAO.delete(follow);
    }
}
