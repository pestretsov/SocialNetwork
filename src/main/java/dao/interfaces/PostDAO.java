package dao.interfaces;

import model.Post;

import java.util.List;

/**
 * Created by artemypestretsov on 7/13/16.
 */
public interface PostDAO extends DAO<Post> {
    List<Post> getAllByFromId(int fromId);
    List<Post> getSublistByFromId(int fromId, int offset, int limit);
    List<Post> getSublistWithOffsetId(int fromId, int offsetId, int limit);
    List<Post> getPersonalTimelineWithOffsetId(int followerId, int offsetId, int limit);
}
