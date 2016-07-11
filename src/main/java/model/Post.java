package model;

/**
 * Created by artemypestretsov on 7/11/16.
 */

import lombok.Data;

@Data
public class Post {
    private int id;
    private int fromId;
    private int postType;
    private String text;
}
