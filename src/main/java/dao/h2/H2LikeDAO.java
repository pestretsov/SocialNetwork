package dao.h2;

import common.cp.ConnectionPool;
import dao.interfaces.LikeDAO;

import java.sql.*;
import java.time.Instant;

/**
 * Created by artemypestretsov on 8/8/16.
 */
public class H2LikeDAO extends H2DAO implements LikeDAO {

    public H2LikeDAO(ConnectionPool connectionPool) {
        super(connectionPool);
    }

    @Override
    public int addLike(int postId, int userId) {
        if (hasLike(postId, userId)) {
            return -1;
        }

        String sql = "INSERT INTO \"Like\" (post_id, user_id, publish_time) VALUES (?,?,?)";

        try (Connection connection = getConnectionPool().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, postId);
            statement.setInt(2, userId);
            statement.setTimestamp(3, Timestamp.from(Instant.now()));

            statement.execute();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                } else {
                    throw new SQLException("No keys have been generated");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean removeLike(int postId, int userId) {
        if (!hasLike(postId, userId)) {
            return false;
        }

        String sql = "DELETE FROM \"Like\" WHERE post_id=? AND user_id=?";

        try (Connection connection = getConnectionPool().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, postId);
            statement.setInt(2, userId);

            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean hasLike(int postId, int userId) {
        String sql = "SELECT post_id, user_id FROM \"Like\" WHERE post_id=? AND user_id=?";
        try (Connection connection = getConnectionPool().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, postId);
            statement.setInt(2, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
               return resultSet.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
