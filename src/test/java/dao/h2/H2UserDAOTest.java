package dao.h2;

import common.cp.ConnectionPool;
import common.cp.SimpleConnectionPool;
import dao.interfaces.UserDAO;
import model.User;
import model.UserGender;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.Assert.*;

/**
 * Created by artemypestretsov on 7/13/16.
 */
public class H2UserDAOTest {
    private static final String RESOURCES_FILE_PATH = "src/test/resources/";
    private static final String DB_PROPERTIES = "db.properties";
    private static final String DB_PREPARE_FILE_NAME = "h2.sql";

    private static ConnectionPool connectionPool;
    private static UserDAO userDAO;

    @BeforeClass
    public static void connectionPoolTest() throws Exception {
        connectionPool = SimpleConnectionPool.create(RESOURCES_FILE_PATH + DB_PROPERTIES);
        connectionPool.executeScript(RESOURCES_FILE_PATH + DB_PREPARE_FILE_NAME);
        userDAO = new H2UserDAO(connectionPool);
    }

    @Test
    public void getUserByIdTest() throws Exception {
        User user = userDAO.getById(1).get();
        assertEquals(user.getUsername(), "ambush");
    }

    @Test
    public void getUserByUsernameTest() throws Exception {
        User user = userDAO.getByUsername("ambush").get();
        assertEquals(user.getId(), 1);
    }

    @Test
    public void deleteUserByIdTest() throws Exception {
        assertTrue(userDAO.deleteById(2));
        Optional<User> userOptional = userDAO.getByUsername("ambush1");
        assertFalse(userOptional.isPresent());
    }

    @Test
    public void createUserTest() throws Exception {
        User user = new User();
        user.setGender(UserGender.getUserGenderById(1));
        user.setBirthDate(LocalDate.now());
        user.setLastName("Novik");
        user.setFirstName("Nikita");
        user.setUsername("insane-nna");
        user.setPassword("pass_word");
        user.setBio("Nothing do here");

        userDAO.create(user);

        Optional<User> user1 = userDAO.getByUsername("insane-nna");
        assertTrue(user1.isPresent());
        assertEquals(user1.get().getId(), 5);

        userDAO.deleteById(user1.get().getId());
    }

    @Test
    public void updateUserTest() throws Exception {
        User user = userDAO.getByUsername("ambush").get();
        user.setBio("not an empty bio");
        assertTrue(userDAO.update(user));
        User user1 = userDAO.getByUsername("ambush").get();
        assertEquals(user.getBio(), "not an empty bio");
    }

    @AfterClass
    public static void closeAll() throws Exception {
        connectionPool.close();
    }

}
