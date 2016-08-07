package model.restmodel;

import lombok.Data;
import model.dbmodel.UserEntity;
import model.dbmodel.UserGenderEntity;

import java.time.LocalDate;

/**
 * Created by artemypestretsov on 8/6/16.
 */
@Data
public class User {
    private int id;

    private String username;
    private String firstName;
    private String lastName;
    private String bio;
    private UserGenderEntity gender;

    private LocalDate birthDate;

    public User(UserEntity user) {
        setId(user.getId());
        setUsername(user.getUsername());

        setLastName(user.getLastName());
        setFirstName(user.getFirstName());
        setBirthDate(user.getBirthDate());
        setGender(user.getGender());

        setBio(user.getBio());
    }
}