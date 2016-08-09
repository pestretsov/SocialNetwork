package dao.interfaces;

import model.Comment;
import model.CommentView;

import java.util.List;

/**
 * Created by artemypestretsov on 8/10/16.
 */
public interface CommentViewDAO {
    List<CommentView> getPostCommentsSublist(int postId, int offsetId, int limit);
}
