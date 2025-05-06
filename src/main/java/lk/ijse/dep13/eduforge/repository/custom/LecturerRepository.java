package lk.ijse.dep13.eduforge.repository.custom;

import lk.ijse.dep13.eduforge.entity.Lecturer;
import lk.ijse.dep13.eduforge.repository.CrudRepository;

import java.util.List;


public interface LecturerRepository extends CrudRepository<Lecturer, Integer> {
    List<Lecturer> findFullTimeLecturers();
    List<Lecturer> findVisitingLecturers();
}
