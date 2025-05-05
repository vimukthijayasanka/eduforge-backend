package lk.ijse.dep13.eduforge.service.impl;

import lk.ijse.dep13.eduforge.dto.request.LecturerReqTO;
import lk.ijse.dep13.eduforge.dto.response.LecturerResTO;
import lk.ijse.dep13.eduforge.service.custom.LecturerService;
import lk.ijse.dep13.eduforge.util.LecturerType;

import java.util.List;

public class LecturerServiceImpl implements LecturerService {
    @Override
    public LecturerResTO saveLecturer(LecturerReqTO lecturerReqTO) {
        return null;
    }

    @Override
    public void updateLecturerDetailsWithImage(LecturerReqTO lecturerReqTO) {

    }

    @Override
    public void updateLecturerDetailsWithoutImage(LecturerReqTO lecturerReqTO) {

    }

    @Override
    public void deleteLecturer(Integer lecturerId) {

    }

    @Override
    public LecturerResTO getLecturerDetails(Integer lecturerId) {
        return null;
    }

    @Override
    public List<LecturerResTO> getAllLecturers(LecturerType lecturerType) {
        return List.of();
    }
}
