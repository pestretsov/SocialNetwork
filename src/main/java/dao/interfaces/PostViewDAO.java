package dao.interfaces;

import model.PostView;

import java.util.List;

/**
 * Created by artemypestretsov on 8/7/16.
 */
public interface PostViewDAO {
    List<PostView> getUserPostsSublist(int userId, int fromId, int offsetId, int limit);
    List<PostView> getPersonalTimeline(int userId, int offsetId, int limit);
}
