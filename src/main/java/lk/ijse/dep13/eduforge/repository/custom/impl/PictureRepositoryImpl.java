package lk.ijse.dep13.eduforge.repository.custom.impl;

import lk.ijse.dep13.eduforge.entity.Lecturer;
import lk.ijse.dep13.eduforge.entity.Picture;
import lk.ijse.dep13.eduforge.repository.CrudRepositoryImpl;
import lk.ijse.dep13.eduforge.repository.custom.PictureRepository;

import java.util.Optional;

public class PictureRepositoryImpl extends CrudRepositoryImpl<Picture, Lecturer> implements PictureRepository {
    @Override
    public void deleteById(Lecturer pk) {
        getEntityManager().remove(getEntityManager().find(Picture.class, pk.getId()));
    }

    @Override
    public Optional<Picture> findById(Lecturer pk) {
        return Optional.ofNullable(getEntityManager().find(Picture.class, pk.getId()));
    }
}
