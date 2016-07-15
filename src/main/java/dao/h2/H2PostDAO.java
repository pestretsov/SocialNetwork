package dao.h2;

import common.cp.ConnectionPool;
import dao.interfaces.PostDAO;
import model.Post;

import java.util.List;
import java.util.Optional;

/**
 * Created by artemypestretsov on 7/13/16.
 */
public class H2PostDAO implements PostDAO {
    private final ConnectionPool connectionPool;

    public H2PostDAO(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public List<Post> getAllPostsByFromId(int fromId) {
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
