package dao.interfaces;

import model.PostView;

import java.util.List;

/**
 * Created by artemypestretsov on 8/7/16.
 */
public interface PostViewDAO {
    List<PostView> getPersonalTimelineWithOffsetId(int followerId, int offsetId, int limit);
    List<PostView> getAllPosts(int fromId);
    List<PostView> getSublist(int fromId, int offsetId, int limit);
}
