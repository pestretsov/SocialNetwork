package dao.h2;

import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import common.cp.ConnectionPool;
import dao.interfaces.UserDAO;
import model.User;
import model.UserGender;
import model.UserRole;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by artemypestretsov on 7/13/16.
 */
public class H2UserDAO extends H2DAO implements UserDAO {

    public H2UserDAO(ConnectionPool connectionPool) {
        super(connectionPool);
    }

    private User parseUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setUsername(resultSet.getString("username"));
        user.setPassword(resultSet.getString("password"));
        user.setFirstName(resultSet.getString("first_name"));
        user.setLastName(resultSet.getString("last_name"));
        user.setGender(UserGender.getUserGenderById(resultSet.getInt("gender")));
        user.setBirthDate(Optional.ofNullable(resultSet.getDate("birth_date")).map(Date::toLocalDate).orElse(null));
        user.setBio(resultSet.getString("bio"));
        user.setRole(UserRole.getUserRoleById(resultSet.getInt("role")));
        return user;
    }

    private Optional<User> parseUserOpt(ResultSet resultSet) throws SQLException {
        return resultSet.next() ? Optional.of(parseUser(resultSet)) : Optional.empty();
    }

    // TODO: Optionals
    private void setUserWithoutId(PreparedStatement statement, User user) throws SQLException {
        statement.setString(1, user.getUsername());
        statement.setString(2, user.getPassword());
        statement.setString(3, user.getFirstName());
        statement.setString(4, user.getLastName());

        int userGender = Optional.ofNullable(user.getGender()).map(UserGender::getIdByUserGender).orElse(UserGender.NOT_SPECIFIED.getId());

        Date birthDate = Optional.ofNullable(user.getBirthDate()).map(Date::valueOf).orElse(null);

        int userRole = Optional.ofNullable(user.getRole()).map(UserRole::getId).orElse(UserRole.USER.getId());

        statement.setInt(5, userGender);
        statement.setDate(6, birthDate);
        statement.setString(7, user.getBio());
        statement.setInt(8, userRole);
    }

    @Override
    public Optional<User> getByUsername(String username) {
        String sql = "SELECT id, username, password, first_name, last_name, gender, birth_date, bio, role FROM User " +
                "WHERE username=?";

        try (Connection connection = getConnectionPool().getConnection();
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
        String sql = "SELECT id, username, password, first_name, last_name, gender, birth_date, bio, role FROM User " +
                "WHERE id=?";

        try (Connection connection = getConnectionPool().getConnection();
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
        String sql = "INSERT INTO User (username, password, first_name, last_name, gender, birth_date, bio, role) " +
                "VALUES (?,?,?,?,?,?,?,?)";

        try (Connection connection = getConnectionPool().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setUserWithoutId(statement, user);
            statement.executeUpdate();
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
        String sql = "UPDATE User SET username=?, password=?, first_name=?, last_name=?, gender=?, birth_date=?, bio=?, role=? WHERE id=?";

        try (Connection connection = getConnectionPool().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            setUserWithoutId(statement, user);
            statement.setInt(9, user.getId());
            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deleteById(int id) {
        String sql = "DELETE FROM User WHERE id=?";

        try (Connection connection = getConnectionPool().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> getUsersFollowingUser(int userId) {
        String sql = "SELECT id, username, password, first_name, last_name, gender, birth_date, bio, role" +
                " FROM User WHERE id IN (SELECT follower_id FROM Follow WHERE user_id=?)";

        List<User> followers = new ArrayList<>();

        try (Connection connection = getConnectionPool().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    followers.add(parseUser(resultSet));
                }
            }

            return followers;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> getUsersFollowedByUser(int userId) {
        String sql = "SELECT id, username, password, first_name, last_name, gender, birth_date, bio, role" +
                " FROM User WHERE id IN (SELECT user_id FROM Follow WHERE follower_id=?)";

        List<User> followings = new ArrayList<>();

        try (Connection connection = getConnectionPool().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    followings.add(parseUser(resultSet));
                }
            }

            return followings;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> getUsersNotFollowedByUser(int userId) {
        String sql = "SELECT id, username, password, first_name, last_name, gender, birth_date, bio, role" +
                " FROM User WHERE id NOT IN (SELECT user_id FROM Follow WHERE follower_id=?) AND id<>?";

        List<User> notFollowedUsers = new ArrayList<>();

        try (Connection connection = getConnectionPool().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setInt(2, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    notFollowedUsers.add(parseUser(resultSet));
                }
            }

            return notFollowedUsers;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        String sql = "SELECT id, username, password, first_name, last_name, gender, birth_date, bio, role" +
                " FROM User";
        List<User> allUsers = new ArrayList<>();

        try (Connection connection = getConnectionPool().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    allUsers.add(parseUser(resultSet));
                }
            }

            return allUsers;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
