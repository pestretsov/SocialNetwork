package dao.interfaces;

import java.util.Optional;

/**
 * Created by artemypestretsov on 7/13/16.
 */
public interface DAO<T> {
    // this method returns auto-generated id
    int create(T model);
    boolean update(T model);

    default Optional<T> getById(int id) {
        return Optional.empty();
    }

    default boolean deleteById(int id) {
        return false;
    };
}
