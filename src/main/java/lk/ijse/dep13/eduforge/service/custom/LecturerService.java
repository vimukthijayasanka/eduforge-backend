package lk.ijse.dep13.eduforge.service.custom;

import lk.ijse.dep13.eduforge.dto.request.LecturerReqTO;
import lk.ijse.dep13.eduforge.dto.response.LecturerTO;
import lk.ijse.dep13.eduforge.repository.custom.LecturerRepository;
import lk.ijse.dep13.eduforge.repository.custom.LinkedInRepository;
import lk.ijse.dep13.eduforge.repository.custom.PictureRepository;
import lk.ijse.dep13.eduforge.service.SuperService;
import lk.ijse.dep13.eduforge.util.LecturerType;

import java.util.List;

public interface LecturerService extends SuperService {
//
//    void setLecturerRepository(LecturerRepository lecturerRepository);
//    void setLinkedInRepository(LinkedInRepository linkedInRepository);
//    void setPictureRepository(PictureRepository pictureRepository);

    LecturerTO saveLecturer(LecturerReqTO lecturerReqTO);

    void updateLecturerDetailsWithImage(LecturerReqTO lecturerReqTO);

    void updateLecturerDetailsWithoutImage(LecturerReqTO lecturerReqTO);

    void deleteLecturer(Integer lecturerId);

    LecturerTO getLecturerDetails(Integer lecturerId);

    List<LecturerTO> getAllLecturers(LecturerType lecturerType);

}
