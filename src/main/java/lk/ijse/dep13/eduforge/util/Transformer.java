package lk.ijse.dep13.eduforge.util;

import lk.ijse.dep13.eduforge.dto.request.LecturerReqTO;
import lk.ijse.dep13.eduforge.dto.response.LecturerTO;
import lk.ijse.dep13.eduforge.entity.Lecturer;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class Transformer {
    private final ModelMapper mapper = new ModelMapper();

    public Lecturer fromLecturerReqTO(LecturerReqTO lecturerReqTO){
        return mapper.map(lecturerReqTO, Lecturer.class);
    }

    public Lecturer fromLecturerResTO(LecturerTO lecturerTO){
        return mapper.map(lecturerTO, Lecturer.class);
    }

    public LecturerTO toLecturerTO(Lecturer lecturer){
        LecturerTO lecturerTO = mapper.map(lecturer, LecturerTO.class);

        // Manually set the LinkedIn URL (if the LinkedIn object is not null)
        if (lecturer.getLinkedin() != null) {
            lecturerTO.setLinkedin(lecturer.getLinkedin().getUrl());
        }

        return lecturerTO;
    }

    public List<LecturerTO> toLecturerTOList(List<Lecturer> lecturerList){
        return lecturerList.stream().map(this::toLecturerTO).collect(Collectors.toList());
    }

}
