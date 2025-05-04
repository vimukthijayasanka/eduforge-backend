package lk.ijse.dep13.eduforge.repository;

import jakarta.persistence.EntityManager;

public interface SuperRepository {
    void setEntityManager(EntityManager entityManager);
}
