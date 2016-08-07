package model;

/**
 * Created by artemypestretsov on 8/6/16.
 */
public enum PostType {
    DEFAULT,
    PUBLIC,
    PRIVATE;

    public static int getIdByPostType(PostType postType) {
        switch (postType) {
            case DEFAULT: return 0;
            case PUBLIC: return 0;
            case PRIVATE: return 1;
        }
        throw new IllegalStateException("Illegal PostType:" + postType.name());
    }

    public static PostType getPostTypeById(int id) {
        switch (id) {
            case 0: return PUBLIC;
            case 1: return PRIVATE;
        }
        throw new IllegalArgumentException("Illegal ID for PostType:" + id);
    }
}
