package model;

import lombok.Data;

import java.time.Instant;

/**
 * Created by artemypestretsov on 8/10/16.
 */
@Data
public class CommentView {
    private int commentId;
    private int postId;
    private String commentText;
    private Instant commentPublishTime;

    private int fromId;
    private String fromUsername;
    private String fromFirstName;
    private String fromLastName;
}
