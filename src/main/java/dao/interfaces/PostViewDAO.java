package dao.interfaces;

import model.PostView;

import java.util.List;

/**
 * Created by artemypestretsov on 8/7/16.
 */
public interface PostViewDAO {
    List<PostView> getUserPostsSublist(int fromId, int offsetId, int limit);
    List<PostView> getPersonalTimeline(int followerId, int offsetId, int limit);
}
