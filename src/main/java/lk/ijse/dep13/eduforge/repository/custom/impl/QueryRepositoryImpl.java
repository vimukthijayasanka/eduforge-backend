package lk.ijse.dep13.eduforge.repository.custom.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lk.ijse.dep13.eduforge.repository.custom.QueryRepository;
import org.springframework.stereotype.Component;

@Component
public class QueryRepositoryImpl implements QueryRepository {

    private EntityManager entityManager;

    @PersistenceContext
    @Override
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
