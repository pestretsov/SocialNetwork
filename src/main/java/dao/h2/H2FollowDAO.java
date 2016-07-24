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

    // the follow status is never updated
    @Override
    public boolean update(Follow model) {
        return false;
    }

    @Override
    public List<User> getAllFollowersByUserId(int user_id) {
        String sql = "SELECT follower_id FROM Follow WHERE user_id=?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, user_id);

            return getAllWithStatement(statement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> getAllFollowingByUserId(int user_id) {
        String sql = "SELECT user_id FROM Follow WHERE follower_id=?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, user_id);

            return getAllWithStatement(statement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<User> getAllWithStatement(PreparedStatement statement) throws SQLException {
        UserDAO userDAO = new H2UserDAO(connectionPool);

        List<User> users = new ArrayList<>();
        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {

                // TODO: needs real implementation
                int userId = resultSet.getInt(1);
                Optional<User> user = userDAO.getById(userId);

                if (user.isPresent()) {
                    users.add(user.get());
                }
            }
        }

        return users;
    }
}
