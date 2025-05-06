package lk.ijse.dep13.eduforge.util;

import lk.ijse.dep13.eduforge.dto.request.LecturerReqTO;
import lk.ijse.dep13.eduforge.dto.response.LecturerTO;
import lk.ijse.dep13.eduforge.entity.Lecturer;
import lk.ijse.dep13.eduforge.entity.LinkedIn;
import lk.ijse.dep13.eduforge.entity.Picture;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class Transformer {
    private final ModelMapper mapper;

    public Transformer(ModelMapper mapper) {
        this.mapper = mapper;
        mapper.typeMap(LinkedIn.class, String.class).setConverter(context -> context.getSource() !=null ? context.getSource().getUrl() : null);
        mapper.typeMap(MultipartFile.class, Picture.class).setConverter(context -> null);
        mapper.typeMap(String.class, LinkedIn.class).setConverter(mappingContext -> mappingContext.getSource() != null ? new LinkedIn(null, mappingContext.getSource()) : null);
    }

    public Lecturer fromLecturerReqTO(LecturerReqTO lecturerReqTO){
       Lecturer lecturer = mapper.map(lecturerReqTO, Lecturer.class);
       if (lecturerReqTO.getLinkedin() != null) lecturer.getLinkedin().setLecturer(lecturer);
       return lecturer;
    }

    public Lecturer fromLecturerTO(LecturerTO lecturerTO){
        Lecturer lecturer = mapper.map(lecturerTO, Lecturer.class);
        if (lecturerTO.getLinkedin() != null) lecturer.getLinkedin().setLecturer(lecturer);
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
