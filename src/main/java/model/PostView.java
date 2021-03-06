package model;

import lombok.Data;

import java.time.Instant;

/**
 * Created by artemypestretsov on 8/7/16.
 */

@Data
public class PostView {
    private int postId;
    private String postText;
    private PostType postType;
    private Instant postPublishTime;

    private int fromId;
    private String fromUsername;
    private String fromFirstName;
    private String fromLastName;

    // not in DB schema:
    private int likeCount;
    private boolean likable;
    private boolean editable;
}
