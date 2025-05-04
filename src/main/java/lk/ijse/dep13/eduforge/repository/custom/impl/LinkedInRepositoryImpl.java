package lk.ijse.dep13.eduforge.repository.custom.impl;

import jakarta.persistence.EntityManager;
import lk.ijse.dep13.eduforge.entity.Lecturer;
import lk.ijse.dep13.eduforge.entity.LinkedIn;
import lk.ijse.dep13.eduforge.entity.SuperEntity;
import lk.ijse.dep13.eduforge.repository.custom.LinkedInRepository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public class LinkedInRepositoryImpl implements LinkedInRepository {

    EntityManager entityManager;

    @Override
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public LinkedIn save(LinkedIn entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public void update(LinkedIn entity) {
        entityManager.merge(entity);
    }

    @Override
    public void deleteById(Lecturer pk) {
        entityManager.remove(entityManager.find(LinkedIn.class, pk));
    }

    @Override
    public boolean existsById(Lecturer pk) {
        return findById(pk).isPresent();
    }

    @Override
    public Optional<LinkedIn> findById(Lecturer pk) {
        return Optional.ofNullable(entityManager.find(LinkedIn.class, pk));
    }

    @Override
    public List<LinkedIn> findAll() {
        return entityManager.createQuery("SELECT l from LinkedIn l",LinkedIn.class).getResultList();
    }

    @Override
    public long count() {
        return entityManager.createQuery("SELECT COUNT(l) FROM LinkedIn l", Long.class).getSingleResult();
    }
}
