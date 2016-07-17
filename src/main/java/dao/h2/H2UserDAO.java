package dao.h2;

import common.cp.ConnectionPool;
import dao.interfaces.UserDAO;
import model.Post;
import model.User;

import java.sql.*;
import java.util.Collection;
import java.util.Optional;

/**
 * Created by artemypestretsov on 7/13/16.
 */
public class H2UserDAO implements UserDAO {
    private final ConnectionPool connectionPool;

    public H2UserDAO(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    private User parseUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setUsername(resultSet.getString("username"));
        user.setPassword(resultSet.getString("password"));
        user.setFirstName(resultSet.getString("first_name"));
        user.setLastName(resultSet.getString("last_name"));
        user.setSex(resultSet.getInt("sex"));
        user.setBirthDate(resultSet.getDate("birth_date").toLocalDate());
        user.setBio(resultSet.getString("bio"));
        return user;
    }

    private Optional<User> parseUserOpt(ResultSet resultSet) throws SQLException {
        return resultSet.next() ? Optional.of(parseUser(resultSet)) : Optional.empty();
    }

    private void setUserWithoutId(PreparedStatement statement, User user) throws SQLException {
        statement.setString(1, user.getUsername());
        statement.setString(2, user.getPassword());
        statement.setString(3, user.getFirstName());
        statement.setString(4, user.getLastName());
        statement.setInt(5, user.getSex());
        statement.setDate(6, Date.valueOf(user.getBirthDate()));
        statement.setString(7, user.getBio());
    }

    @Override
    public Optional<User> getByUsername(String username) {
        String sql = "SELECT id, username, password, first_name, last_name, sex, birth_date, bio FROM User " +
                "WHERE username=?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                return parseUserOpt(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> getById(int id) {
        String sql = "SELECT id, username, password, first_name, last_name, sex, birth_date, bio FROM User " +
                "WHERE id=?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                return parseUserOpt(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int create(User user) {
        String sql = "INSERT INTO User (username, password, first_name, last_name, sex, birth_date, bio) " +
                "VALUES (?,?,?,?,?,?,?)";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            setUserWithoutId(statement, user);
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
    public boolean update(User user) {
        String sql = "UPDATE User SET username=?, password=?, first_name=?, last_name=?, sex=?, birth_date=?, bio=? WHERE id=?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            setUserWithoutId(statement, user);
            statement.setInt(8, user.getId());
            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deleteById(int id) {
        String sql = "DELETE FROM User WHERE id=?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
