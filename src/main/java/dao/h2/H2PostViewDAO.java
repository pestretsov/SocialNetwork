package dao.h2;

import common.cp.ConnectionPool;
import dao.interfaces.PostViewDAO;
import model.PostType;
import model.PostView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by artemypestretsov on 8/7/16.
 */
public class H2PostViewDAO implements PostViewDAO {
    private final ConnectionPool connectionPool;

    public H2PostViewDAO(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    private PostView parsePostView(ResultSet resultSet) throws SQLException {
        PostView postView = new PostView();

        postView.setPostId(resultSet.getInt("post_id"));
        postView.setPostText(resultSet.getString("post_text"));
        postView.setPostType(PostType.getPostTypeById(resultSet.getInt("post_type")));
        postView.setPostPublishTime(resultSet.getTimestamp("post_publish_time").toInstant());

        postView.setFromId(resultSet.getInt("from_id"));
        postView.setFromUsername(resultSet.getString("from_username"));
        postView.setFromFirstName(resultSet.getString("from_first_name"));
        postView.setFromFirstName(resultSet.getString("from_last_name"));

        return postView;
    }

    @Override
    public List<PostView> getUserPostsSublist(int fromId, int offsetId, int limit) {
        List<PostView> sublist = new ArrayList<>();

        String sql = "SELECT post_id, post_text, post_type, post_publish_time, from_id, from_username," +
                " from_first_name, from_last_name FROM PostView WHERE from_id=? AND post_id<? ORDER BY post_id DESC LIMIT ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, fromId);
            statement.setInt(2, offsetId);
            statement.setInt(3, limit);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    sublist.add(parsePostView(resultSet));
                }
            }
            return sublist;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<PostView> getPersonalTimeline(int followerId, int offsetId, int limit) {
        List<PostView> timeline = new ArrayList<>();

        String sql =
                "SELECT post_id, post_text, post_type, post_publish_time, from_id, " +
                        "from_username, from_first_name, from_last_name FROM PostView " +
                        "WHERE from_id IN (SELECT user_id FROM Follow WHERE follower_id=?) AND post_id<? ORDER BY post_id DESC LIMIT ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, followerId);
            statement.setInt(2, offsetId);
            statement.setInt(3, limit);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    timeline.add(parsePostView(resultSet));
                }
            }
            return timeline;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
