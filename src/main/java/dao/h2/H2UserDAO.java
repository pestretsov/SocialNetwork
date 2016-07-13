package dao.h2;

import common.cp.ConnectionPool;
import dao.interfaces.UserDAO;
import model.User;

import java.util.Optional;

/**
 * Created by artemypestretsov on 7/13/16.
 */
public class H2UserDAO implements UserDAO {
    private final ConnectionPool connectionPool;

    public H2UserDAO(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return null;
    }

    @Override
    public boolean create(Object model) {
        return false;
    }

    @Override
    public boolean update(Object model) {
        return false;
    }

    @Override
    public Optional getById(int id) {
        return null;
    }

    @Override
    public boolean deleteById(int id) {
        return false;
    }
}
