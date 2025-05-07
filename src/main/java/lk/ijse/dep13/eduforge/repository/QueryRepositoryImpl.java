package lk.ijse.dep13.eduforge.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;

@Component
public class QueryRepositoryImpl implements QueryRepository {

    @PersistenceContext
    private EntityManager entityManager;
}
