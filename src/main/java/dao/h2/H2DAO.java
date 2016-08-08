package dao.h2;

import common.cp.ConnectionPool;

/**
 * Created by artemypestretsov on 8/8/16.
 */
public class H2DAO {
    private final ConnectionPool connectionPool;
    public H2DAO(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    protected final ConnectionPool getConnectionPool() {
        return connectionPool;
    };
}
