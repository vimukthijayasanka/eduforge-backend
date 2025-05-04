package lk.ijse.dep13.eduforge.repository;

import lk.ijse.dep13.eduforge.entity.SuperEntity;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface CrudRepository extends SuperRepository {

    SuperEntity save(SuperEntity entity);

    void update(SuperEntity entity);

    void deleteById(Serializable pk);

    boolean existsById(Serializable pk);

    Optional<SuperEntity> findById(Serializable pk);

    List<SuperEntity> findAll();

    long count();
}
