package lk.ijse.dep13.eduforge.repository.custom.impl;

import jakarta.persistence.EntityManager;
import lk.ijse.dep13.eduforge.repository.custom.QueryRepository;

public class QueryRepositoryImpl implements QueryRepository {

    private EntityManager entityManager;

    @Override
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
