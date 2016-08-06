package dao.interfaces;

import model.dbmodel.FollowEntity;

/**
 * Created by artemypestretsov on 7/23/16.
 */
public interface FollowDAO extends DAO<FollowEntity> {
    FollowEntity getByUserAndFollowerId(int userId, int followerId);
}
