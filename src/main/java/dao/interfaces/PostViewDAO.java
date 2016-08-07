package dao.interfaces;

import model.dbmodel.PostEntity;
import model.dbmodel.PostView;

import java.util.List;

/**
 * Created by artemypestretsov on 8/7/16.
 */
public interface PostViewDAO extends DAO<PostView> {
    List<PostView> getPersonalTimelineWithOffsetId(int followerId, int offsetId, int limit);
}
