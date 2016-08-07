package model.dbmodel;

/**
 * Created by artemypestretsov on 8/7/16.
 */
public enum UserRoleEntity {
    ADMIN,
    USER;

    public static int getIdByUserRole(UserRoleEntity role) {
        switch (role) {
            case USER: return 0;
            case ADMIN: return 1;
        }
        throw new IllegalStateException("Illegal user sex:" + role.name());
    }

    public static UserRoleEntity getUserRoleById(int id) {
        switch (id) {
            case 0: return USER;
            case 1: return ADMIN;
        }
        throw new IllegalArgumentException("Illegal id for user role:" + id);
    }
}
