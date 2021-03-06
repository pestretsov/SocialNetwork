package model;

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

    private String username; // username VARCHAR(255) NOT NULL UNIQUE

    private String password; // password VARCHAR(255) NOT NULL

    private String firstName; // first_name VARCHAR(255) NOT NULL

    private String lastName; // last_name VARCHAR(255) NOT NULL

    private UserRole role;

    // Optional:
    private UserGender gender; // gender INT DEFAULT 0 (0– not specified; 1 – female; 2 – male)
    private LocalDate birthDate; // birth_date DATE
    private String bio; // bio TEXT
}