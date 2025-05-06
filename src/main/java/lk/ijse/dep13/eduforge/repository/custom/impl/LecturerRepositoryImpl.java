package lk.ijse.dep13.eduforge.repository.custom.impl;

import lk.ijse.dep13.eduforge.entity.Lecturer;
import lk.ijse.dep13.eduforge.repository.CrudRepositoryImpl;
import lk.ijse.dep13.eduforge.repository.custom.LecturerRepository;

import java.util.List;

public class LecturerRepositoryImpl extends CrudRepositoryImpl<Lecturer, Integer> implements LecturerRepository {

    @Override
    public List<Lecturer> findFullTimeLecturers() {
        return getEntityManager().createQuery("SELECT e FROM Lecturer e WHERE e.type = lk.ijse.dep13.eduforge.util.LecturerType.FULL_TIME", Lecturer.class).getResultList();
    }

    @Override
    public List<Lecturer> findVisitingLecturers() {
        return getEntityManager().createQuery("SELECT e FROM Lecturer e WHERE e.type = lk.ijse.dep13.eduforge.util.LecturerType.VISITING", Lecturer.class).getResultList();

    }
}
