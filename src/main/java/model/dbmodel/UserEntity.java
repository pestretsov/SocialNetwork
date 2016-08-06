package model.dbmodel;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

/**
 * Created by artemypestretsov on 7/11/16.
 */

@Data
public class User {
    private int id; // id INT PRIMARY KEY AUTO_INCREMENT

    @Size(max = 255)
    @NotNull
    private String username; // username VARCHAR(255) NOT NULL UNIQUE

    @Size(max = 255)
    @NotNull
    private String password; // password VARCHAR(255) NOT NULL

    @Size(max = 255)
    @NotNull
    private String firstName; // first_name VARCHAR(255) NOT NULL

    @Size(max = 255)
    @NotNull
    private String lastName; // last_name VARCHAR(255) NOT NULL

    // Optional:
    private int sex; // sex INT DEFAULT 0 (0– not specified; 1 – female; 2 – male)
    private LocalDate birthDate; // birth_date DATE
    private String bio; // bio TEXT
}