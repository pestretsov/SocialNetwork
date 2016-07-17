package model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.sql.Clob;
import java.time.Instant;

/**
 * Created by artemypestretsov on 7/11/16.
 */

@Data
public class Post {
    private int id; // id INT PRIMARY KEY AUTO_INCREMENT

    @NotNull
    private int fromId; // from_id INT NOT NULL REFERENCES User(id)

    private int postType; // post_type INT DEFAULT 0 (0 - public; 1 - private)

    private String text; // text TEXT

    @NotNull
    private Instant publishTime; // publish_time TIMESTAMP NOT NULL
}