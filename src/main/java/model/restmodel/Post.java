package model.restmodel;

import lombok.Data;
import model.dbmodel.PostEntity;
import model.dbmodel.PostTypeEntity;

import java.time.Instant;

/**
 * Created by artemypestretsov on 8/6/16.
 */
@Data
public class Post {
    private int id;
    private User from;

    private String text;

    private Instant publishTime;
    private PostTypeEntity postType;

    public Post(PostEntity post, User user) {
        setId(post.getId());
        setFrom(user);
        setText(post.getText());
        setPublishTime(post.getPublishTime());
        setPostType(post.getPostType());
    }
}
