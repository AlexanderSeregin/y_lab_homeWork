package website.ylab.learningplatform.datasource;

import java.util.Optional;

public interface Dao<T> {

    Optional<T> get(long id);

    void save(T t);

    void delete(T t);

    void update(T t);

    Iterable<T> getAll();

}
