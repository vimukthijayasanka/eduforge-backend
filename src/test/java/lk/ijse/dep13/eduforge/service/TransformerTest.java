package lk.ijse.dep13.eduforge.service;

import jakarta.transaction.Transactional;
import lk.ijse.dep13.eduforge.dto.request.LecturerReqTO;
import lk.ijse.dep13.eduforge.dto.LecturerTO;
import lk.ijse.dep13.eduforge.entity.Lecturer;
import lk.ijse.dep13.eduforge.entity.LinkedIn;
import lk.ijse.dep13.eduforge.util.LecturerType;
import lk.ijse.dep13.eduforge.service.util.Transformer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class TransformerTest {

    @Autowired
    private Transformer transformer;

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

    @Test
    void fromLecturerTo(){
        LecturerTO lecturerTO = new LecturerTO(2, "Joel Miller", "Probationary lecturer", "Bsc in Management and Finance", LecturerType.FULL_TIME, 5, null, "https://linkedin.com/joel-miller");
        Lecturer lecturer = transformer.fromLecturerTO(lecturerTO);
        assertEquals(lecturerTO.getId(), lecturer.getId());
        assertEquals(lecturerTO.getName(), lecturer.getName());
        assertEquals(lecturerTO.getDesignation(), lecturer.getDesignation());
        assertEquals(lecturerTO.getQualification(), lecturer.getQualification());
        assertEquals(lecturerTO.getDisplayOrder(), lecturer.getDisplayOrder());
        assertEquals(lecturerTO.getLinkedin(), lecturer.getLinkedin().getUrl());
    }

    @Test
    void fromLecturerReqTO(){
        LecturerReqTO lecturerReqTO = new LecturerReqTO(
                "Abby",
                "Senior Lecturer",
                "MSc in Nuclear Physics",
                LecturerType.FULL_TIME,
                10,
                null,
                "https://linkedin.com/abby"
        );
        Lecturer lecturer = transformer.fromLecturerReqTO(lecturerReqTO);
        assertEquals(lecturerReqTO.getName(), lecturer.getName());
        assertEquals(lecturerReqTO.getDesignation(), lecturer.getDesignation());
        assertEquals(lecturerReqTO.getQualification(), lecturer.getQualification());
        assertEquals(lecturerReqTO.getType(), lecturer.getType());
        assertEquals(lecturerReqTO.getDisplayOrder(), lecturer.getDisplayOrder());
        assertEquals(lecturerReqTO.getLinkedin(), lecturer.getLinkedin().getUrl());
    }
}
