package website.ylab.learningplatform.datasource;

import java.util.Optional;

public interface Dao<T> {

    Iterable<T> getAll();

}
