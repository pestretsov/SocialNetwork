package dao.interfaces;

import model.Comment;

import java.util.List;

/**
 * Created by artemypestretsov on 8/9/16.
 */
public interface CommentDAO {
    int addPostComment(Comment comment);
    Comment getById(int id);
    boolean deleteById(int id);
    boolean removePostComment(Comment comment);
}
