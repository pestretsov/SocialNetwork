package dao.interfaces;

import model.Follow;

/**
 * Created by artemypestretsov on 7/23/16.
 */
public interface FollowDAO extends DAO<Follow> {
    Follow getByUserAndFollowerId(int userId, int followerId);
    int getAllFollowingsByUser(int userId);
    int getAllFollowersByUser(int userId);
    boolean isFollowing(int userId, int followerId);
    boolean delete(Follow follow);
}
