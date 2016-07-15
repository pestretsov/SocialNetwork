package common.cp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.function.Supplier;

/**
 * Created by artemypestretsov on 7/13/16.
 */
public class SimpleConnectionPool implements ConnectionPool {

    private BlockingQueue<Connection> freeConnections;
    private BlockingQueue<Connection> reservedConnections;
    private volatile boolean isClosing = false;

    public static ConnectionPool create(String propertiesFile) {
        try {
            return create(new FileInputStream(propertiesFile));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static ConnectionPool create(InputStream stream) {
        Properties properties = new Properties();
        try {
            properties.load(stream);
            return create(properties);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ConnectionPool create(Properties properties) {
        String user = properties.getProperty("user", "");
        String password = properties.getProperty("password", "");

        if (!properties.contains("driver") || !properties.contains("url")) {
            throw new IllegalArgumentException("Cannot find driver or url property");
        }

        String driver = properties.getProperty("driver");
        String url = properties.getProperty("url");

        int poolSize = Integer.parseInt(properties.getProperty("poolSize", "1"));

        return create(driver, url, user, password, poolSize);
    }

    public static ConnectionPool create(String driver, String url, String user, String password, int poolSize) {
        return new SimpleConnectionPool(poolSize, () -> {
            try {
                Class.forName(driver);
                return DriverManager.getConnection(url, user, password);
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private SimpleConnectionPool(int poolSize, Supplier<Connection> connectionSupplier) {
        freeConnections = new ArrayBlockingQueue<Connection>(poolSize);
        reservedConnections = new ArrayBlockingQueue<Connection>(poolSize);

        for (int i = 0; i < poolSize; i++) {
            freeConnections.add(connectionSupplier.get());
        }
    }

    private ConnectionWrapper wrap(Connection connection) {
        return new ConnectionWrapper(connection) {
            @Override
            public void close() throws SQLException {
                if (connection.isClosed()) {
                    throw new SQLException("Connection is already closed");
                }

                if (connection.isReadOnly()) {
                    connection.setReadOnly(false);
                }

                if (reservedConnections.contains(this) && !reservedConnections.remove(this)) {
                    throw new RuntimeException("Cannot close reserved connection");
                }

                if (isClosing) {
                    connection.close();
                } else if (!freeConnections.offer(connection)) {
                    throw new RuntimeException("Cannot return connection to the pool");
                }
            }
        };
    }

    // TODO: check polymorphism
    @Override
    public Connection getConnection() {
        if (isClosing) {
            throw new RuntimeException("Cannot get connection from the closing pool");
        }

        try {
            final Connection connection = freeConnections.take();
            reservedConnections.add(connection);
            return connection;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws Exception {
        isClosing = true;

        freeConnections.forEach(connection -> {
            try {
                connection.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
