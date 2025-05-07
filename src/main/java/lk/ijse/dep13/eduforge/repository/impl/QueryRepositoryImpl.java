package lk.ijse.dep13.eduforge.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lk.ijse.dep13.eduforge.repository.QueryRepository;
import org.springframework.stereotype.Component;

@Component
public class QueryRepositoryImpl implements QueryRepository {

    @PersistenceContext
    private EntityManager entityManager;
}
