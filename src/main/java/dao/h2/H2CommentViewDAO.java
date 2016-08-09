package dao.h2;

import common.cp.ConnectionPool;
import dao.interfaces.CommentDAO;
import dao.interfaces.CommentViewDAO;
import model.CommentView;
import model.PostType;
import model.PostView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by artemypestretsov on 8/10/16.
 */
public class H2CommentViewDAO extends H2DAO implements CommentViewDAO {
    public H2CommentViewDAO(ConnectionPool connectionPool) {
        super(connectionPool);
    }

    private CommentView parseCommentView(ResultSet resultSet) throws SQLException {
        CommentView commentView = new CommentView();

        commentView.setCommentId(resultSet.getInt("comment_id"));
        commentView.setPostId(resultSet.getInt("post_id"));
        commentView.setCommentText(resultSet.getString("comment_text"));
        commentView.setCommentPublishTime(resultSet.getTimestamp("comment_publish_time").toInstant());

        commentView.setFromId(resultSet.getInt("from_id"));
        commentView.setFromUsername(resultSet.getString("from_username"));
        commentView.setFromFirstName(resultSet.getString("from_first_name"));
        commentView.setFromLastName(resultSet.getString("from_last_name"));

        return commentView;
    }

    @Override
    public List<CommentView> getPostCommentsSublist(int postId, int offsetId, int limit) {
        List<CommentView> sublist = new ArrayList<>();

        String sql = "SELECT comment_id, post_id, comment_text, comment_publish_time, from_id, " +
                "from_username, from_first_name, from_last_name FROM CommentView " +
                "WHERE post_id=? AND comment_id>? ORDER BY comment_id ASC LIMIT ?";

        try (Connection connection = getConnectionPool().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, postId);
            statement.setInt(2, offsetId);
            statement.setInt(3, limit);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    sublist.add(parseCommentView(resultSet));
                }
            }
            return sublist;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
