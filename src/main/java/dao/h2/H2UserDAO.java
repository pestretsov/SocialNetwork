package dao.h2;

import common.cp.ConnectionPool;
import dao.interfaces.UserDAO;
import model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
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
        user.setFirstName(resultSet.getString("first_name"));
        user.setLastName(resultSet.getString("last_name"));
        user.setSex(resultSet.getInt("sex"));
        user.setBirthDate(resultSet.getDate("birth_date").toLocalDate());
        return user;
    }

    private Optional<User> parseUserOpt(ResultSet resultSet) throws SQLException {
        return resultSet.next() ? Optional.of(parseUser(resultSet)) : Optional.empty();
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        "SELECT id, username, password, first_name, last_name," + "sex, birth_date, bio FROM USER " +
                "WHERE id=?";
        return null;
    }

    @Override
    public Optional<User> getById(int id) {
        return null;
    }

    @Override
    public int create(User model) {
        return -1;
    }

    @Override
    public boolean update(User model) {
        return false;
    }


    @Override
    public boolean deleteById(int id) {
        return false;
    }
}
