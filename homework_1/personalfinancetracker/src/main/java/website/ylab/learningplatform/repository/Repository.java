package website.ylab.learningplatform.repository;

import java.util.List;
import java.util.Optional;

/**
 * Generic repository interface for CRUD operations
 *
 * @param <T>  Entity type
 * @param <ID> ID type
 */
public interface Repository<T, ID> {
    /**
     * Find entity by ID
     *
     * @param id entity ID
     * @return Optional containing entity if found
     */
    Optional<T> findById(ID id);

    /**
     * Save entity
     *
     * @param entity entity to save
     * @return saved entity
     */
    T save(T entity);

    /**
     * Find all entities
     *
     * @return list of all entities
     */
    List<T> findAll();

    /**
     * Delete entity
     *
     * @param entity entity to delete
     */
    void delete(T entity);

    /**
     * Delete entity by ID
     *
     * @param id entity ID
     */
    void deleteById(ID id);
}
