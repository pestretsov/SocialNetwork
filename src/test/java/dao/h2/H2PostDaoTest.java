package dao.h2;

import common.cp.ConnectionPool;
import common.cp.SimpleConnectionPool;
import dao.interfaces.UserDAO;
import model.User;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by artemypestretsov on 7/13/16.
 */
public class H2PostDAOTest {
    private static final String RESOURCES_FILE_PATH = "src/test/resources/";
    private static final String DB_PROPERTIES = "db.properties";
    private static final String DB_PREPARE_FILE_NAME = "h2.sql";

    private static ConnectionPool connectionPool;

    @BeforeClass
    public static void connectionPoolTest() throws Exception {
        connectionPool = SimpleConnectionPool.create(RESOURCES_FILE_PATH + DB_PROPERTIES);
    }

    @Before
    public void executeQueryOnConnectionPoolTest() throws Exception {
        connectionPool.executeScript(RESOURCES_FILE_PATH + DB_PREPARE_FILE_NAME);
    }

    @Test
    public void getPostByIdTest() throws Exception {
    }

    @AfterClass
    public static void closeAll() throws Exception {
        connectionPool.close();
    }
}
