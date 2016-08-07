package model;

/**
 * Created by artemypestretsov on 8/7/16.
 */
public enum UserRole {
    ADMIN,
    USER;

    public static int getIdByUserRole(UserRole role) {
        switch (role) {
            case USER: return 0;
            case ADMIN: return 1;
        }
        throw new IllegalStateException("Illegal user role:" + role.name());
    }

    public static UserRole getUserRoleById(int id) {
        switch (id) {
            case 0: return USER;
            case 1: return ADMIN;
        }
        throw new IllegalArgumentException("Illegal id for user role:" + id);
    }
}
