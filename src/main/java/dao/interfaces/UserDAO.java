package dao.interfaces;

import model.User;
import java.util.Optional;

/**
 * Created by artemypestretsov on 7/13/16.
 */
public interface UserDAO extends DAO<User> {
    Optional<User> getByUsername(String username);
    // TODO: getAllFriends()
}
