package dao.h2;

import common.cp.ConnectionPool;
import common.cp.ConnectionWrapper;
import dao.interfaces.PostDAO;
import model.Post;
import model.PostType;

import java.sql.*;
import java.util.*;
import java.util.stream.Stream;

/**
 * Created by artemypestretsov on 7/13/16.
 */
public class H2PostDAO implements PostDAO {
    private final ConnectionPool connectionPool;

    public H2PostDAO(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    private Post parsePost(ResultSet resultSet) throws SQLException {
        Post post = new Post();
        post.setId(resultSet.getInt("id"));
        post.setFromId(resultSet.getInt("from_id"));
        post.setPostType(resultSet.getInt("post_type"));
        post.setText(resultSet.getString("text"));
        post.setPublishTime(resultSet.getTimestamp("publish_time").toInstant());
        return post;
    }

    private Optional<Post> parsePostOpt(ResultSet resultSet) throws SQLException {
        return resultSet.next() ? Optional.of(parsePost(resultSet)) : Optional.empty();
    }

    private void setPostWithoutId(PreparedStatement statement, Post post) throws SQLException {
        statement.setInt(1, post.getFromId());
        statement.setInt(2, post.getPostType());
        statement.setString(3, post.getText());
        statement.setTimestamp(4, Timestamp.from(post.getPublishTime()));
    }

    @Override
    public List<Post> getAllByFromId(int fromId) {
        List<Post> allPosts = new ArrayList<>();

        String sql = "SELECT id, from_id, post_type, text, publish_time FROM Post WHERE from_id=? ORDER BY id DESC";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, fromId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    allPosts.add(parsePost(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return allPosts;
    }

    @Override
    public List<Post> getSublistWithOffsetId(int fromId, int offsetId, int limit) {
        List<Post> allPosts = new ArrayList<>();

        String sql = "SELECT id, from_id, post_type, text, publish_time FROM Post WHERE from_id=? AND id<? ORDER BY id DESC LIMIT ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, fromId);
            statement.setInt(2, offsetId);
            statement.setInt(3, limit);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    allPosts.add(parsePost(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return allPosts;
    }

    @Override
    public List<Post> getSublistByFromId(int fromId, int offset, int limit) {
        List<Post> allPosts = new ArrayList<>();

        String sql = "SELECT id, from_id, post_type, text, publish_time FROM Post WHERE from_id=? ORDER BY id DESC LIMIT ? OFFSET ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, fromId);
            statement.setInt(2, limit);
            statement.setInt(3, offset);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    allPosts.add(parsePost(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return allPosts;
    }

//    @Override
//    public List<Post> getPersonalTimelineWithOffsetId(int followerId, int offsetId, int limit) {
//        List<Post> allPosts = new ArrayList<>();
//
//        String sql = "SELECT id, from_id, post_type, text, publish_time FROM Post WHERE from_id IN (SELECT user_id FROM Follow WHERE follower_id=?) AND id<? ORDER BY id DESC LIMIT ?";
//
//        try (Connection connection = connectionPool.getConnection();
//             PreparedStatement statement = connection.prepareStatement(sql)) {
//            statement.setInt(1, followerId);
//            statement.setInt(2, offsetId);
//            statement.setInt(3, limit);
//            try (ResultSet resultSet = statement.executeQuery()) {
//                while (resultSet.next()) {
//                    allPosts.add(parsePost(resultSet));
//                }
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//
//        return allPosts;
//    }

    @Override
    public int create(Post post) {
        String sql = "INSERT INTO Post (from_id, post_type, text, publish_time) VALUES (?,?,?,?)";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setPostWithoutId(statement, post);
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
    public boolean update(Post post) {
        String sql = "UPDATE Post SET from_id=?, post_type=?, text=?, publish_time=? WHERE id=?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            setPostWithoutId(statement, post);
            statement.setInt(5, post.getId());
            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Post> getById(int id) {
        String sql = "SELECT id, from_id, post_type, text, publish_time FROM Post WHERE id=?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                return parsePostOpt(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deleteById(int id) {
        String sql = "DELETE FROM Post WHERE id=?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
