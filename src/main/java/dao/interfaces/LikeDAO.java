package dao.interfaces;

/**
 * Created by artemypestretsov on 8/8/16.
 */
public interface LikeDAO {
    int addLike(int postId, int userId);
    int removeLike(int postId, int userId);
}