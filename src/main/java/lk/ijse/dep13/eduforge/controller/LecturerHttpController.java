package lk.ijse.dep13.eduforge.controller;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lk.ijse.dep13.eduforge.dto.request.LecturerReqTO;
import lk.ijse.dep13.eduforge.dto.response.LecturerResTO;
import lk.ijse.dep13.eduforge.entity.Lecturer;
import lk.ijse.dep13.eduforge.entity.LinkedIn;
import lk.ijse.dep13.eduforge.entity.Picture;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/lecturers")
@CrossOrigin
public class LecturerHttpController {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private Bucket bucket;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = "multipart/form-data", produces = "application/json")
    public LecturerResTO createNewLecturer(@ModelAttribute @Validated(LecturerReqTO.Create.class) LecturerReqTO lecturerReqTO){
        System.out.println(lecturerReqTO);
        entityManager.getTransaction().begin();
        try{
            Lecturer lecturer = modelMapper.map(lecturerReqTO, Lecturer.class);
            lecturer.setPicture(null); // model mapper create new object if picture is empty, so have to make it null
            lecturer.setLinkedin(null);
            entityManager.persist(lecturer);
            LecturerResTO lecturerResTO = modelMapper.map(lecturer, LecturerResTO.class);

            if (lecturerReqTO.getPicture() != null) {
                Picture picture = new Picture(lecturer, "lectures/" + lecturer.getId());
                entityManager.persist(picture);
                Blob blobId = bucket.create(picture.getPicturePath(), lecturerReqTO.getPicture().getInputStream(), lecturerReqTO.getPicture().getContentType());
                lecturerResTO.setPicturePath(blobId.signUrl(1, TimeUnit.DAYS, Storage.SignUrlOption.withV4Signature()).toString());
            }
            if (lecturerReqTO.getLinkedin() != null) {
                entityManager.persist(new LinkedIn(lecturer, lecturerReqTO.getLinkedin()));
                lecturerResTO.setLinkedin(lecturerReqTO.getLinkedin());
            }

            entityManager.getTransaction().commit();
            return lecturerResTO;
        }catch (IOException t){
            entityManager.getTransaction().rollback();
            throw new RuntimeException(t);
        }
    }

    @GetMapping(value = "/{lecturer-id}", produces = "application/json")
    public LecturerResTO getLecturerDetails(@PathVariable("lecturer-id") Integer lecturerId){
        Lecturer lecturer = entityManager.find(Lecturer.class, lecturerId);
        if (lecturer == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lecturer not found" );
        LecturerResTO lecturerResTO = modelMapper.map(lecturer, LecturerResTO.class);
        if (lecturer.getLinkedin() != null) lecturerResTO.setLinkedin(lecturer.getLinkedin().getUrl());
        if (lecturer.getPicture() != null) {
            lecturerResTO.setPicturePath(bucket.get(lecturer.getPicture().getPicturePath()).signUrl(1, TimeUnit.DAYS, Storage.SignUrlOption.withV4Signature()).toString());
        }
        return lecturerResTO;
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
    public List<LecturerResTO> getAllLecturers(){
        TypedQuery<Lecturer> query = entityManager.createQuery("SELECT l FROM Lecturer l", Lecturer.class);
        return getLecturerTOList(query);
    }

    @GetMapping(params = "type=full-time",produces = "application/json")
    public List<LecturerResTO> getFullTimeLecturers(){
        TypedQuery<Lecturer> query = entityManager.createQuery("SELECT l FROM Lecturer l WHERE l.type = lk.ijse.dep13.eduforge.util.LecturerType.FULL_TIME", Lecturer.class);
        return getLecturerTOList(query);
    }

    @GetMapping(params = "type=visiting", produces = "application/json")
    public List<LecturerResTO> getVisitingLecturers(){
        TypedQuery<Lecturer> query = entityManager.createQuery("SELECT l FROM Lecturer l WHERE l.type = lk.ijse.dep13.eduforge.util.LecturerType.VISITING", Lecturer.class);
        return getLecturerTOList(query);
    }

    private List<LecturerResTO> getLecturerTOList(TypedQuery<Lecturer> query) {
        return query.getResultStream().map(lecturerEntity -> {
            LecturerResTO lecturerResTO = modelMapper.map(lecturerEntity, LecturerResTO.class);
            if (lecturerEntity.getLinkedin() != null) lecturerResTO.setLinkedin(lecturerEntity.getLinkedin().getUrl());
            if (lecturerEntity.getPicture() != null) {
                lecturerResTO.setPicturePath(bucket.get(lecturerEntity.getPicture().getPicturePath()).signUrl(1, TimeUnit.DAYS, Storage.SignUrlOption.withV4Signature()).toString());
            }
            return lecturerResTO;
        }).collect(Collectors.toList());
    }
}
