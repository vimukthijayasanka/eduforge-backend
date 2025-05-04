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
    public SuperEntity save(SuperEntity entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public void update(SuperEntity entity) {
        entityManager.merge(entity);
    }

    @Override
    public void deleteById(Serializable pk) {
        entityManager.remove(entityManager.find(Lecturer.class, pk));
    }

    @Override
    public boolean existsById(Serializable pk) {
        return entityManager.find(Lecturer.class, pk) != null;
    }

    @Override
    public Optional<SuperEntity> findById(Serializable pk) {
        return Optional.ofNullable(entityManager.find(Lecturer.class, pk));
    }

    @Override
    public List<SuperEntity> findAll() {
        return entityManager.createQuery("SELECT l from Lecturer l").getResultList();
    }

    @Override
    public long count() {
        return entityManager.createQuery("SELECT COUNT(l) FROM Lecturer l", Long.class).getSingleResult();
    }
}
