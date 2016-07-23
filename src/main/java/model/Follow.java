package model;

import lombok.Data;

/**
 * Created by artemypestretsov on 7/23/16.
 */

@Data
public class Follow {
    // follower is following followingId;
    private int followerId;
    private int followingId;
}
