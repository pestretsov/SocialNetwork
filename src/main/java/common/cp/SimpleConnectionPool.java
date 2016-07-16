package common.cp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Created by artemypestretsov on 7/13/16.
 */
public class SimpleConnectionPool implements ConnectionPool {

    private BlockingQueue<Connection> freeConnections;
    private BlockingQueue<Connection> reservedConnections;
    private volatile boolean isClosing = false;

    public static ConnectionPool create(String propertiesFile) {
        try (InputStream inputStream = new FileInputStream(propertiesFile)) {
            return create(inputStream);
        } catch (IOException e) {
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

        if (!properties.containsKey("driver")) {
            throw new IllegalArgumentException("Cannot find driver");
        }

        if (!properties.containsKey("url")) {
            throw new IllegalArgumentException("Cannot find url");
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
        freeConnections = new ArrayBlockingQueue<>(poolSize);
        reservedConnections = new ArrayBlockingQueue<>(poolSize);

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
            Connection connection = wrap(freeConnections.take());
            reservedConnections.add(connection);
            return connection;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void executeScript(String prepareFilePath) {
        executeScript(Paths.get(prepareFilePath));
    }

    private void executeScript(Path path) {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            String[] sqls = getStatementsFromFile(path);

            for (String sql : sqls) {
                statement.addBatch(sql);
            }

            statement.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException("Error executing script", e);
        }
    }

    private String[] getStatementsFromFile(Path filePath) {
        try {
            return Files.lines(filePath)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.joining())
                    .split(";");
        } catch (IOException e) {
            throw new RuntimeException("Parsing SQL script file error", e);
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
