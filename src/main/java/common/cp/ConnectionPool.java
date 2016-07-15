package common.cp;

import java.sql.Connection;

/**
 * Created by artemypestretsov on 7/13/16.
 */
public interface ConnectionPool extends AutoCloseable {
    Connection getConnection();
    void executeScript(String file);
}
