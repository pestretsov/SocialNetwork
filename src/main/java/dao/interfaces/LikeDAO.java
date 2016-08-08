package dao.interfaces;

import model.Like;

/**
 * Created by artemypestretsov on 8/8/16.
 */
public interface LikeDAO {
    int addLike(int postId, int userId);
    int addLike(Like like);

    boolean removeLike(int postId, int userId);
    boolean removeLike(Like like);

    boolean hasLike(int postId, int userId);
}