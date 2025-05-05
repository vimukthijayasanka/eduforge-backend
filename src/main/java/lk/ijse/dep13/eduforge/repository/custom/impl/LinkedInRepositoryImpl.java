package lk.ijse.dep13.eduforge.repository.custom.impl;

import lk.ijse.dep13.eduforge.entity.Lecturer;
import lk.ijse.dep13.eduforge.entity.LinkedIn;
import lk.ijse.dep13.eduforge.repository.CrudRepositoryImpl;
import lk.ijse.dep13.eduforge.repository.custom.LinkedInRepository;

import java.util.Optional;

public class LinkedInRepositoryImpl extends CrudRepositoryImpl<LinkedIn, Lecturer> implements LinkedInRepository{
    @Override
    public void deleteById(Lecturer pk) {
        getEntityManager().remove(getEntityManager().find(LinkedIn.class, pk.getId()));
    }

    @Override
    public Optional<LinkedIn> findById(Lecturer pk) {
        return Optional.ofNullable(getEntityManager().find(LinkedIn.class, pk.getId()));
    }
}
