package model;

import lombok.Data;

/**
 * Created by artemypestretsov on 7/23/16.
 */

@Data
public class Follow {
    // followerId is following userId;
    private int followerId;
    private int userId;
}
