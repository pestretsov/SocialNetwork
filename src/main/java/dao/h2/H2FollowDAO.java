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
                resultSet.next();
                return parseFollow(resultSet);
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
