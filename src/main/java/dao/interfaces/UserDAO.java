package dao.interfaces;

import model.User;

import java.util.List;
import java.util.Optional;

/**
 * Created by artemypestretsov on 7/13/16.
 */
public interface UserDAO extends DAO<User> {
    Optional<User> getByUsername(String username);
    List<User> getUsersFollowingUser(int userId);
    List<User> getUsersFollowedByUser(int userId);
}
