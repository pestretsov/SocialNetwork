package common.cp;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by artemypestretsov on 7/13/16.
 */
public class SimpleConnectionPoolTest {
    private static final String RESOURCES_FILE_PATH = "src/test/resources/";
    private static final String DB_PROPERTIES_FILE_NAME = "db.properties";
    private static final String DB_PREPARE_FILE_NAME = "h2.sql";

    private static ConnectionPool connectionPool;

    @BeforeClass
    public void init() throws Exception {
        connectionPool = SimpleConnectionPool.create("");

    }


}
