package lk.ijse.dep13.eduforge.repository;

import jakarta.persistence.EntityManager;
import lk.ijse.dep13.eduforge.entity.SuperEntity;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;

public abstract class CrudRepositoryImpl<T extends SuperEntity, ID extends java.io.Serializable> implements CrudRepository<T, ID> {

    private EntityManager entityManager;

    private final Class<T> entityClzObj;

    public CrudRepositoryImpl(){
        entityClzObj = (Class<T>)((ParameterizedType)(this.getClass().getGenericSuperclass())).getActualTypeArguments()[0];
    }

    @Override
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    public T save(T entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public void update(T entity) {
        entityManager.merge(entity);
    }

    @Override
    public void deleteById(ID pk) {
        entityManager.remove(entityManager.find(entityClzObj, pk));
    }

    @Override
    public boolean existsById(ID pk) {
        return findById(pk).isPresent();
    }

    @Override
    public Optional<T> findById(ID pk) {
        return Optional.ofNullable(entityManager.find(entityClzObj, pk));
    }

    @Override
    public List<T> findAll() {
        return entityManager.createQuery("SELECT e FROM " + entityClzObj.getName() + " e", entityClzObj).getResultList();
    }

    @Override
    public long count() {
        return entityManager.createQuery("SELECT COUNT(e) FROM " + entityClzObj.getName() + " e", Long.class).getSingleResult();
    }
}
