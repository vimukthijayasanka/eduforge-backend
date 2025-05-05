package lk.ijse.dep13.eduForge.service;

import lk.ijse.dep13.eduforge.WebAppConfig;
import lk.ijse.dep13.eduforge.WebRootConfig;
import lk.ijse.dep13.eduforge.dto.response.LecturerTO;
import lk.ijse.dep13.eduforge.entity.Lecturer;
import lk.ijse.dep13.eduforge.entity.LinkedIn;
import lk.ijse.dep13.eduforge.util.LecturerType;
import lk.ijse.dep13.eduforge.util.Transformer;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitWebConfig(classes = {WebAppConfig.class, WebRootConfig.class})
public class TransformerTest {

    private final Transformer transformer = new Transformer();

    @Test
    void toLecturerTO(){
        Lecturer lecturer = new Lecturer(2, "Joel Miller", "Probationary lecturer", "Bsc in Management and Finance", LecturerType.FULL_TIME, 5);
        lecturer.setLinkedin(new LinkedIn(lecturer, "https://linkedin.com/joel-miller"));
        LecturerTO lecturerTO = transformer.toLecturerTO(lecturer);

        System.out.println(lecturerTO);

        assertEquals(lecturer.getId(), lecturerTO.getId());
        assertEquals(lecturer.getName(), lecturerTO.getName());
        assertEquals(lecturer.getDesignation(), lecturerTO.getDesignation());
        assertEquals(lecturer.getQualification(), lecturerTO.getQualification());
        assertEquals(lecturer.getType(), lecturerTO.getType());
        assertEquals(lecturer.getLinkedin().getUrl(), lecturerTO.getLinkedin());
    }
}
