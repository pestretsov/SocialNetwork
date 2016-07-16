package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.Instant;

/**
 * Created by artemypestretsov on 7/11/16.
 */

@Data
public class Post {
    private int id;
    private int fromId;
    private int postType;
    private String text;
    private Instant publishTime;
}
