package dao.h2;

import common.cp.ConnectionPool;
import dao.interfaces.CommentDAO;
import model.Comment;
import model.Post;
import model.PostType;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by artemypestretsov on 8/9/16.
 */
public class H2CommentDAO extends H2DAO implements CommentDAO {
    public H2CommentDAO(ConnectionPool connectionPool) {
        super(connectionPool);
    }

    private Comment parseComment(ResultSet resultSet) throws SQLException {
        Comment comment = new Comment();
        comment.setId(resultSet.getInt("id"));
        comment.setPostId(resultSet.getInt("post_id"));
        comment.setFromId(resultSet.getInt("from_id"));
        comment.setText(resultSet.getString("text"));
        comment.setPublishTime(resultSet.getTimestamp("publish_time").toInstant());

        return comment;
    }

    @Override
    public int addPostComment(Comment comment) {
        String sql = "INSERT INTO Comment (post_id, from_id, text, publish_time) VALUES (?,?,?,?)";

        try (Connection connection = getConnectionPool().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            Timestamp publishTime = Optional.ofNullable(comment.getPublishTime())
                    .map(Timestamp::from).orElse(Timestamp.from(Instant.now()));

            statement.setInt(1, comment.getPostId());
            statement.setInt(2, comment.getFromId());
            statement.setString(3, comment.getText());
            statement.setTimestamp(4, publishTime);

            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                } else {
                    throw new SQLException("No keys have been generated");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Comment getById(int id) {
        String sql = "SELECT id, post_id, from_id, text, publish_time FROM Comment WHERE id=?";

        try (Connection connection = getConnectionPool().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return parseComment(resultSet);
                } else {
                    throw new SQLException();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean removePostComment(Comment comment) {
        return deleteById(comment.getId());
    }

    @Override
    public boolean deleteById(int id) {
        String sql = "DELETE FROM Comment WHERE id=?";

        try (Connection connection = getConnectionPool().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
