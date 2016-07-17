package common.cp;

import org.junit.*;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by artemypestretsov on 7/13/16.
 */
public class SimpleConnectionPoolTest {
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
    public void getConnectionFromConnectionPoolTest() throws Exception {
        Connection connection = connectionPool.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM USER");

        int count = 0;

        while (resultSet.next()) {
            String name = resultSet.getString("username");
            count++;
        }

        assertEquals(count, 4);
    }

    @AfterClass
    public static void closeAll() throws Exception {
        connectionPool.close();
    }
}
