package dao.interfaces;

import model.Post;
import java.util.List;
import java.util.Optional;

/**
 * Created by artemypestretsov on 7/13/16.
 */
public interface PostDAO extends DAO {
    List<Post> getAllPostsByFromId(int fromId);
}
