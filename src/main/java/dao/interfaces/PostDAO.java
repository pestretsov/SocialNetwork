package dao.interfaces;

import model.Post;
import java.util.List;
import java.util.Optional;

/**
 * Created by artemypestretsov on 7/13/16.
 */
public interface PostDAO extends DAO<Post> {
    List<Post> getAllByFromId(int fromId);
    List<Post> getSublistByFromId(int fromId, int offset, int limit);
    Optional<Post> getLatestPostByFromId(int fromId);
}
