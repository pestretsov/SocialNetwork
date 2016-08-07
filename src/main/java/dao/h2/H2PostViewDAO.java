package dao.h2;

import dao.interfaces.PostViewDAO;
import model.dbmodel.PostView;

import java.util.List;

/**
 * Created by artemypestretsov on 8/7/16.
 */
public class H2PostViewDAO implements PostViewDAO {
    @Override
    public int create(PostView model) {
        return 0;
    }

    @Override
    public boolean update(PostView model) {
        return false;
    }

    @Override
    public List<PostView> getPersonalTimelineWithOffsetId(int followerId, int offsetId, int limit) {
//        String sql =
        return null;
    }
}
