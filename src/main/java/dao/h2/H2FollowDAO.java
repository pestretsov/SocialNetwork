package dao.h2;

import common.cp.ConnectionPool;
import dao.interfaces.FollowDAO;
import dao.interfaces.UserDAO;
import model.Follow;
import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public int create(Follow follow) {
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

    public boolean delete(Follow follow) {
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
    public boolean update(Follow follow) {
        return delete(follow);
    }

}
