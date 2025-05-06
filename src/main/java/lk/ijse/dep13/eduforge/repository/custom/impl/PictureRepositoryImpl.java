package lk.ijse.dep13.eduforge.repository.custom.impl;

import lk.ijse.dep13.eduforge.entity.Picture;
import lk.ijse.dep13.eduforge.repository.CrudRepositoryImpl;
import lk.ijse.dep13.eduforge.repository.custom.PictureRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PictureRepositoryImpl extends CrudRepositoryImpl<Picture, Integer> implements PictureRepository {

}
