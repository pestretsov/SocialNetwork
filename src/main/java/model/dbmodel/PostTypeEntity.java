package model.dbmodel;

/**
 * Created by artemypestretsov on 8/6/16.
 */
public enum PostTypeEntity {
    DEFAULT,
    PUBLIC,
    PRIVATE;

    public int getId() {
        switch (this) {
            case DEFAULT: return 0;
            case PUBLIC: return 0;
            case PRIVATE: return 1;
        }
        throw new IllegalStateException("Illegal PostPrivacyType:" + this.name());
    }

    public static PostTypeEntity getTypeByID(int id) {
        switch (id) {
            case 0: return PUBLIC;
            case 1: return PRIVATE;
        }
        throw new IllegalArgumentException("Illegal ID for PostPrivacyType:" + id);
    }
}
