package lk.ijse.dep13.eduforge.repository;

import lk.ijse.dep13.eduforge.entity.Lecturer;
import lk.ijse.dep13.eduforge.util.LecturerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LecturerRepository extends JpaRepository<Lecturer, Integer> {

    List<Lecturer> findLecturersByType(LecturerType type);

    @Query("SELECT l FROM Lecturer l WHERE l.type = lk.ijse.dep13.eduforge.util.LecturerType.FULL_TIME")
    List<Lecturer> findFullTimeLecturers();
    @Query("SELECT l FROM Lecturer l WHERE l.type = lk.ijse.dep13.eduforge.util.LecturerType.VISITING")
    List<Lecturer> findVisitingLecturers();
}
