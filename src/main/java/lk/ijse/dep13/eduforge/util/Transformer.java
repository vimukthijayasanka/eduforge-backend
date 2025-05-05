package lk.ijse.dep13.eduforge.util;

import lk.ijse.dep13.eduforge.dto.request.LecturerReqTO;
import lk.ijse.dep13.eduforge.dto.response.LecturerTO;
import lk.ijse.dep13.eduforge.entity.Lecturer;
import lk.ijse.dep13.eduforge.entity.LinkedIn;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class Transformer {
    private final ModelMapper mapper = new ModelMapper();

    public Transformer() {
        mapper.typeMap(LinkedIn.class, String.class).setConverter(context -> context.getSource().getUrl());
        mapper.typeMap(String.class, LinkedIn.class).setConverter(mappingContext -> new LinkedIn(null, mappingContext.getSource()));
    }

    public Lecturer fromLecturerReqTO(LecturerReqTO lecturerReqTO){
       Lecturer lecturer = mapper.map(lecturerReqTO, Lecturer.class);
       if (lecturerReqTO.getLinkedin() == null) {
           lecturer.setLinkedin(null);
       } else{
           lecturer.getLinkedin().setLecturer(lecturer);
       }
       if (lecturerReqTO.getPicture() == null || lecturerReqTO.getPicture().isEmpty()) {
           lecturer.setPicture(null);
       }
       return lecturer;
    }

    public Lecturer fromLecturerTO(LecturerTO lecturerTO){
        Lecturer lecturer = mapper.map(lecturerTO, Lecturer.class);
        lecturer.getLinkedin().setLecturer(lecturer);
        return lecturer;
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
