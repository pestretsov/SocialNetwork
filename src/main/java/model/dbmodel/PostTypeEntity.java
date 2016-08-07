package model.dbmodel;

/**
 * Created by artemypestretsov on 8/6/16.
 */
public enum PostTypeEntity {
    PUBLIC,
    PRIVATE;

    public static int getIdByPostType(PostTypeEntity postType) {
        switch (postType) {
            case PUBLIC: return 0;
            case PRIVATE: return 1;
        }
        throw new IllegalStateException("Illegal PostType:" + postType.name());
    }

    public static PostTypeEntity getPostTypeById(int id) {
        switch (id) {
            case 0: return PUBLIC;
            case 1: return PRIVATE;
        }
        throw new IllegalArgumentException("Illegal ID for PostType:" + id);
    }
}
