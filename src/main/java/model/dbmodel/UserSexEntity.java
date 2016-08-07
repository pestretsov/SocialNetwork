package model.dbmodel;

/**
 * Created by artemypestretsov on 8/7/16.
 */
public enum UserSexEntity {
    NOT_SPECIFIED,
    FEMALE,
    MALE;

    public static int getIdByUserSex(UserSexEntity sex) {
        switch (sex) {
            case NOT_SPECIFIED: return 0;
            case FEMALE: return 1;
            case MALE: return 2;
        }
        throw new IllegalStateException("Illegal user sex:" + sex.name());
    }

    public static UserSexEntity getUserSexById(int id) {
        switch (id) {
            case 0: return NOT_SPECIFIED;
            case 1: return FEMALE;
            case 2: return MALE;
        }
        throw new IllegalArgumentException("Illegal id for user sex:" + id);
    }
}
