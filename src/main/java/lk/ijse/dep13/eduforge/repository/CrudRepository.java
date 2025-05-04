package lk.ijse.dep13.eduforge.repository;

import lk.ijse.dep13.eduforge.entity.SuperEntity;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface CrudRepository<T extends SuperEntity, ID extends Serializable> extends SuperRepository {

    T save(T entity);

    void update(T entity);

    void deleteById(ID pk);

    boolean existsById(ID pk);

    Optional<T> findById(ID pk);

    List<T> findAll();

    long count();
}
