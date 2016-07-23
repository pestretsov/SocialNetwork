package dao.h2;

import dao.interfaces.FollowDAO;
import model.Follow;
import model.User;

import java.util.List;

/**
 * Created by artemypestretsov on 7/23/16.
 */
public class H2FollowDAO implements FollowDAO {
    @Override
    public int create(Follow model) {
        return 0;
    }

    @Override
    public boolean update(Follow model) {
        return false;
    }

    @Override
    public List<User> getAllFollowersByUserId(int id) {
        return null;
    }

    @Override
    public List<User> getAllFollowingByUserId(int id) {
        return null;
    }
}
