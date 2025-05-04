package lk.ijse.dep13.eduforge.repository;

import lk.ijse.dep13.eduforge.entity.Lecturer;

import java.util.List;

public interface LecturerRepository {

    Lecturer saveLecturer(Lecturer lecturer);

    void updateLecturer(Lecturer lecturer);

    void deleteLecturerById(String id);

    boolean existsLecturerById(String id);

    Lecturer findLecturerById(String id);

    List<Lecturer> findAllLecturers();

    long countLecturers();
}
