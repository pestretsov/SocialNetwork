package dao.h2;

import common.cp.ConnectionPool;
import dao.interfaces.FollowDAO;
import model.Follow;

import java.sql.*;

/**
 * Created by artemypestretsov on 7/23/16.
 */
public class H2FollowDAO extends H2DAO implements FollowDAO {

    public H2FollowDAO(ConnectionPool connectionPool) {
        super(connectionPool);
    }

    /**
     * @return this method always returns 0, cause no id is created inside DB
     */
    @Override
    public int create(Follow follow) {
        if (follow.getUserId() == follow.getFollowerId()) {
            return -1;
        }

        String sql = "INSERT INTO Follow (follower_id, user_id) VALUES (?,?)";
        try (Connection connection = getConnectionPool().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, follow.getFollowerId());
            statement.setInt(2, follow.getUserId());

            statement.executeUpdate();

            return 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(Follow follow) {
        String sql = "DELETE FROM Follow WHERE follower_id=? AND user_id=?";
        try (Connection connection = getConnectionPool().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, follow.getFollowerId());
            statement.setInt(2, follow.getUserId());
            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // the only follow update is delete
    @Override
    public boolean update(Follow follow) {
        return delete(follow);
    }

    @Override
    public Follow getByUserAndFollowerId(int userId, int followerId) {
        String sql = "SELECT user_id, follower_id FROM Follow WHERE user_id=? AND follower_id=? ";

        try (Connection connection = getConnectionPool().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setInt(2, followerId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return parseFollow(resultSet);
                } else {
                    throw new SQLException("cannot find this relation");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getAllFollowingsByUser(int followerId) {
        String sql = "SELECT COUNT(*) AS followings_count FROM Follow WHERE follower_id=? AND user_id<>?";

        try (Connection connection = getConnectionPool().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, followerId);
            statement.setInt(2, followerId);
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                return resultSet.getInt("followings_count");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getAllFollowersByUser(int userId) {
        String sql = "SELECT COUNT(*) AS followers_count FROM Follow WHERE user_id=? AND follower_id<>?";

        try (Connection connection = getConnectionPool().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setInt(2, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                return resultSet.getInt("followers_count");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isFollowing(int userId, int followerId) {
        String sql = "SELECT user_id FROM Follow WHERE user_id=? AND follower_id=?";
        try (Connection c = getConnectionPool().getConnection();
             PreparedStatement statement = c.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setInt(2, followerId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Follow parseFollow(ResultSet resultSet) throws SQLException {
        Follow follow = new Follow();
        follow.setUserId(resultSet.getInt("user_id"));
        follow.setFollowerId(resultSet.getInt("follower_id"));

        return follow;
    }
}
