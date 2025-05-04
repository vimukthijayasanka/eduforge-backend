package lk.ijse.dep13.eduforge.repository;

import lk.ijse.dep13.eduforge.entity.Lecturer;
import lk.ijse.dep13.eduforge.entity.Picture;

import java.util.List;

public interface PictureRepository {

    Picture savePicture(Picture picture);

    void updatePicture(Picture picture);

    void deletePictureByLecturer(Lecturer lecturer);

    boolean existsPictureByLecturer(Lecturer lecturer);

    Picture findPictureByLecturer(Lecturer lecturer);

    List<Picture> findAllPictures();

    long countPictures();
}
