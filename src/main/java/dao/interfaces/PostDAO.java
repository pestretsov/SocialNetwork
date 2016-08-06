package dao.interfaces;

import model.dbmodel.PostEntity;

import java.util.List;

/**
 * Created by artemypestretsov on 7/13/16.
 */
public interface PostDAO extends DAO<PostEntity> {
    List<PostEntity> getAllByFromId(int fromId);
    List<PostEntity> getSublistByFromId(int fromId, int offset, int limit);
    List<PostEntity> getSublistWithOffsetId(int fromId, int offsetId, int limit);
//    List<PostEntity> getPersonalTimelineWithOffsetId(int followerId, int offsetId, int limit);
}
