package lk.ijse.dep13.eduforge.repository.custom.impl;

import jakarta.persistence.EntityManager;
import lk.ijse.dep13.eduforge.entity.Lecturer;
import lk.ijse.dep13.eduforge.entity.SuperEntity;
import lk.ijse.dep13.eduforge.repository.custom.LecturerRepository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public class LecturerRepositoryImpl implements LecturerRepository {

    private EntityManager entityManager;

    @Override
    public void setEntityManager(EntityManager entityManager) {
            this.entityManager = entityManager;
    }

    @Override
    public Lecturer save(Lecturer entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public void update(Lecturer entity) {
        entityManager.merge(entity);
    }

    @Override
    public void deleteById(Integer pk) {
        entityManager.remove(entityManager.find(Lecturer.class, pk));
    }

    @Override
    public boolean existsById(Integer pk) {
        return findById(pk).isPresent();
    }

    @Override
    public Optional<Lecturer> findById(Integer pk) {
        return Optional.ofNullable(entityManager.find(Lecturer.class, pk));
    }

    @Override
    public List<Lecturer> findAll() {
        return entityManager.createQuery("SELECT l from Lecturer l",Lecturer.class).getResultList();
    }

    @Override
    public long count() {
        return entityManager.createQuery("SELECT COUNT(l) FROM Lecturer l", Long.class).getSingleResult();
    }
}
