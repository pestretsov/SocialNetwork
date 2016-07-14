package common.cp;

import java.sql.Connection;
import java.util.List;

/**
 * Created by artemypestretsov on 7/13/16.
 */
public class SimpleConnectionPool implements ConnectionPool {
    private List<Connection> freeConnections;
    private List<Connection> reservedConnections;

    public static SimpleConnectionPool create(String propertiesFile) {
        return null;
    }

    @Override
    public Connection getConnection() {

        return null;
    }

    @Override
    public void close() throws Exception {

    }


}
