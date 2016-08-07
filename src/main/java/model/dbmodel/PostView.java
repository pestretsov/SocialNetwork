package model.dbmodel;

import lombok.Data;

import java.time.Instant;

/**
 * Created by artemypestretsov on 8/7/16.
 */

@Data
public class PostView {
    private int postId;
    private String postText;
    private int postType;
    private Instant postPublishTime;
    private int fromId;

    private String fromIdUsername;
    private String fromIdFirstName;
    private String fromIdLastName;
}
