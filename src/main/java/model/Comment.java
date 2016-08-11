package model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;

/**
 * Created by artemypestretsov on 8/9/16.
 */

@Data
public class Comment {
    private int id; // id INT PRIMARY KEY AUTO_INCREMENT

    private int fromId; // from_id INT NOT NULL REFERENCES User(id)

    private int postId; // post_id INT NOT NULL REFERENCES Post(id)

    private String text; // text TEXT

    private Instant publishTime; // publish_time TIMESTAMP NOT NULL
}