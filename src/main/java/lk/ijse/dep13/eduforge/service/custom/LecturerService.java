package lk.ijse.dep13.eduforge.service.custom;

import lk.ijse.dep13.eduforge.dto.request.LecturerReqTO;
import lk.ijse.dep13.eduforge.dto.response.LecturerResTO;
import lk.ijse.dep13.eduforge.entity.Lecturer;
import lk.ijse.dep13.eduforge.service.SuperService;
import lk.ijse.dep13.eduforge.util.LecturerType;

import java.util.List;

public interface LecturerService extends SuperService {
    LecturerResTO saveLecturer(LecturerReqTO lecturerReqTO);

    void updateLecturerDetailsWithImage(LecturerReqTO lecturerReqTO);

    void updateLecturerDetailsWithoutImage(LecturerReqTO lecturerReqTO);

    void deleteLecturer(Integer lecturerId);

    LecturerResTO getLecturerDetails(Integer lecturerId);

    List<LecturerResTO> getAllLecturers(LecturerType lecturerType);

}
