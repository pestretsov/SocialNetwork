package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;

/**
 * Created by artemypestretsov on 7/11/16.
 */

@Data
public class User {
    private int id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;

    // Optional:
    private int sex;
    private LocalDate birthDate;
    private String bio;
}
