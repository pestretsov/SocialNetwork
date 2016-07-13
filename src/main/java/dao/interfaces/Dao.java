package dao.interfaces;

import java.util.Optional;

/**
 * Created by artemypestretsov on 7/13/16.
 */
public interface DAO<T> {
    boolean create(T model);
    boolean update(T model);
    Optional<T> getById(int id);
    boolean deleteById(int id);
}
