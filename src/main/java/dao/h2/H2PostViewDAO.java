package dao.h2;

import common.cp.ConnectionPool;
import dao.interfaces.PostViewDAO;
import model.Post;
import model.PostType;
import model.PostView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by artemypestretsov on 8/7/16.
 */
public class H2PostViewDAO extends H2DAO implements PostViewDAO {

    public H2PostViewDAO(ConnectionPool connectionPool) {
        super(connectionPool);
    }

    private PostView parsePostView(ResultSet resultSet, boolean onlySchema) throws SQLException {
        PostView postView = new PostView();

        postView.setPostId(resultSet.getInt("post_id"));
        postView.setPostText(resultSet.getString("post_text"));
        postView.setPostType(PostType.getPostTypeById(resultSet.getInt("post_type")));
        postView.setPostPublishTime(resultSet.getTimestamp("post_publish_time").toInstant());

        postView.setFromId(resultSet.getInt("from_id"));
        postView.setFromUsername(resultSet.getString("from_username"));
        postView.setFromFirstName(resultSet.getString("from_first_name"));
        postView.setFromLastName(resultSet.getString("from_last_name"));
        postView.setLikeCount(resultSet.getInt("like_count"));

        if (!onlySchema) {
            postView.setLikable(resultSet.getInt("likable") == 0);
            postView.setEditable(resultSet.getInt("editable") > 0);
        }

        return postView;
    }

    private Optional<PostView> parsePostViewOpt(ResultSet resultSet, boolean onlySchema) throws SQLException {
        return resultSet.next() ? Optional.of(parsePostView(resultSet, onlySchema)) : Optional.empty();
    }

    public Optional<PostView> getPostViewById(int postId) {
        String sql = "SELECT post_id, post_text, post_type, post_publish_time, from_id, " +
                "from_username, from_first_name, from_last_name, like_count FROM PostView WHERE post_id=?";

        try (Connection connection = getConnectionPool().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, postId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return parsePostViewOpt(resultSet, true);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<PostView> getUserPostsSublist(int userId, int fromId, int offsetId, int limit) {
        List<PostView> sublist = new ArrayList<>();

        String sql = "SELECT post_id, post_text, post_type, post_publish_time, from_id, " +
                "from_username, from_first_name, from_last_name, like_count, " +
                "(SELECT COUNT(\"Like\".id) FROM \"Like\" " +
                "WHERE \"Like\".post_id=PostView.post_id AND \"Like\".user_id=? GROUP BY \"Like\".id) AS likable, " +
                "(SELECT COUNT(PostView.post_id) FROM PostView pv WHERE pv.from_id=? AND pv.post_id=PostView.post_id) AS editable " +
                "FROM PostView " +
                "WHERE from_id=? AND" +
                " post_id<? ORDER BY post_id DESC LIMIT ?";

        try (Connection connection = getConnectionPool().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setInt(2, userId);
            statement.setInt(3, fromId);
            statement.setInt(4, offsetId);
            statement.setInt(5, limit);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    sublist.add(parsePostView(resultSet, false));
                }
            }
            return sublist;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<PostView> getPersonalTimeline(int userId, int offsetId, int limit) {
        List<PostView> timeline = new ArrayList<>();

        String sql = "SELECT post_id, post_text, post_type, post_publish_time," +
                " from_id, from_username, from_first_name, from_last_name, like_count, " +
                "(SELECT COUNT(\"Like\".id) FROM \"Like\" WHERE \"Like\".post_id=PostView.post_id AND \"Like\".user_id=? GROUP BY \"Like\".id) AS likable, " +
                "(SELECT COUNT(PostView.post_id) FROM PostView pv WHERE pv.from_id=? AND pv.post_id=PostView.post_id) AS editable " +
                "FROM PostView " +
                "WHERE (from_id IN " +
                "(SELECT user_id FROM Follow WHERE follower_id=?) OR from_id=?) AND" +
                " post_id<? ORDER BY post_id DESC LIMIT ?";

        try (Connection connection = getConnectionPool().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setInt(2, userId);
            statement.setInt(3, userId);
            statement.setInt(4, userId);
            statement.setInt(5, offsetId);
            statement.setInt(6, limit);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    timeline.add(parsePostView(resultSet, false));
                }
            }
            return timeline;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
