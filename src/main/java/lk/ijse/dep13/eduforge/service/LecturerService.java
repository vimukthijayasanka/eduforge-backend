package lk.ijse.dep13.eduforge.service;

import lk.ijse.dep13.eduforge.dto.request.LecturerReqTO;
import lk.ijse.dep13.eduforge.dto.LecturerTO;
import lk.ijse.dep13.eduforge.util.LecturerType;

import java.util.List;

public interface LecturerService extends SuperService {
//
//    void setLecturerRepository(LecturerRepository lecturerRepository);
//    void setLinkedInRepository(LinkedInRepository linkedInRepository);
//    void setPictureRepository(PictureRepository pictureRepository);

    LecturerTO saveLecturer(LecturerReqTO lecturerReqTO);

    void updateLecturerDetails(LecturerReqTO lecturerReqTO);

    void updateLecturerDetails(LecturerTO lecturerTO);

    void deleteLecturer(Integer lecturerId);

    LecturerTO getLecturerDetails(Integer lecturerId);

    List<LecturerTO> getAllLecturers(LecturerType lecturerType);

}
