package dao.interfaces;

import model.dbmodel.UserEntity;

import java.util.List;
import java.util.Optional;

/**
 * Created by artemypestretsov on 7/13/16.
 */
public interface UserDAO extends DAO<UserEntity> {
    Optional<UserEntity> getByUsername(String username);
    List<UserEntity> getUsersFollowingUser(int userId);
    List<UserEntity> getUsersFollowedByUser(int userId);
}
