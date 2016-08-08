package model;

/**
 * Created by artemypestretsov on 8/7/16.
 */
public enum UserGender {
    NOT_SPECIFIED,
    FEMALE,
    MALE;

    public int getId() {
        return UserGender.getIdByUserGender(this);
    }

    public static int getIdByUserGender(UserGender gender) {
        switch (gender) {
            case NOT_SPECIFIED: return 0;
            case FEMALE: return 1;
            case MALE: return 2;
        }
        throw new IllegalStateException("Illegal user gender:" + gender.name());
    }

    public static UserGender getUserGenderById(int id) {
        switch (id) {
            case 0: return NOT_SPECIFIED;
            case 1: return FEMALE;
            case 2: return MALE;
        }
        throw new IllegalArgumentException("Illegal id for user gender:" + id);
    }
}
