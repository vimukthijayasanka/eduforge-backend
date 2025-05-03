package lk.ijse.dep13.eduforge.controller;

import jakarta.persistence.EntityManager;
import lk.ijse.dep13.eduforge.dto.request.LecturerReqTO;
import lk.ijse.dep13.eduforge.entity.Lecturer;
import lk.ijse.dep13.eduforge.entity.LinkedIn;
import lk.ijse.dep13.eduforge.entity.Picture;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/lecturers")
@CrossOrigin
public class LecturerHttpController {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ModelMapper modelMapper;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = "multipart/form-data", produces = "application/json")
    public void createNewLecturer(@ModelAttribute @Validated(LecturerReqTO.Create.class) LecturerReqTO lecturerReqTO){
        System.out.println(lecturerReqTO);
        entityManager.getTransaction().begin();
        try{
            Lecturer lecturer = modelMapper.map(lecturerReqTO, Lecturer.class);
            lecturer.setPicture(null); // model mapper create new object if picture is empty, so have to make it null
            lecturer.setLinkedin(null);
            entityManager.persist(lecturer);

            if (lecturerReqTO.getPicture() != null) {
                entityManager.persist(new Picture(lecturer, "lectures/" + lecturer.getId()));
            }
            if (lecturerReqTO.getLinkedin() != null) {
                entityManager.persist(new LinkedIn(lecturer, lecturerReqTO.getLinkedin()));
            }
            entityManager.getTransaction().commit();
        }catch (Throwable t){
            entityManager.getTransaction().rollback();
            throw t;
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(value = "/{lecturer-id}", consumes = "multipart/form-data")
    public void updateLecturerDetailsViaMultipart(@PathVariable("lecturer-id") Integer lecturerId){}

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(value = "/{lecturer-id}", consumes = "application/json")
    public void updateLecturerDetailsViaJson(@PathVariable("lecturer-id") Integer lecturerId){}

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{lecturer-id}")
    public void deleteLecturer(@PathVariable("lecturer-id") Integer lecturerId){}

    @GetMapping(produces = "application/json")
    public void getAllLecturers(){}

    @GetMapping(value = "/{lecturer-id}", produces = "application/json")
    public void getLecturerDetails(){}

    @GetMapping(params = "type=full-time",produces = "application/json")
    public void getFullTimeLecturers(){}

    @GetMapping(params = "type=visiting", produces = "application/json")
    public void getVisitingLecturers(){}
}
