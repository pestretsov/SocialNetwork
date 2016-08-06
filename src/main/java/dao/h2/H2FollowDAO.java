package dao.h2;

import common.cp.ConnectionPool;
import dao.interfaces.FollowDAO;
import model.dbmodel.FollowEntity;

import java.sql.*;

/**
 * Created by artemypestretsov on 7/23/16.
 */
public class H2FollowDAO implements FollowDAO {
    private final ConnectionPool connectionPool;

    public H2FollowDAO(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    /**
     * @return this method always returns 0, cause no id is created inside DB
     */
    @Override
    public int create(FollowEntity follow) {
        String sql = "INSERT INTO Follow (follower_id, user_id) VALUES (?,?)";
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, follow.getFollowerId());
            statement.setInt(2, follow.getUserId());

            statement.executeUpdate();

            return 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean delete(FollowEntity follow) {
        String sql = "DELETE FROM Follow WHERE follower_id=? AND user_id=?";
        try (Connection connection = connectionPool.getConnection();
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
    public boolean update(FollowEntity follow) {
        return delete(follow);
    }

    @Override
    public FollowEntity getByUserAndFollowerId(int userId, int followerId) {
        String sql = "SELECT user_id, follower_id FROM Follow WHERE user_id=? AND follower_id=? ";

        try (Connection connection = connectionPool.getConnection();
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

    private FollowEntity parseFollow(ResultSet resultSet) throws SQLException {
        FollowEntity follow = new FollowEntity();
        follow.setUserId(resultSet.getInt("user_id"));
        follow.setFollowerId(resultSet.getInt("follower_id"));

        return follow;
    }
}
