package lk.ijse.dep13.eduforge.controller;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lk.ijse.dep13.eduforge.dto.request.LecturerReqTO;
import lk.ijse.dep13.eduforge.dto.response.LecturerTO;
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
    public LecturerTO createNewLecturer(@ModelAttribute @Validated(LecturerReqTO.Create.class) LecturerReqTO lecturerReqTO){
        System.out.println(lecturerReqTO);
        entityManager.getTransaction().begin();
        try{
            Lecturer lecturer = modelMapper.map(lecturerReqTO, Lecturer.class);
            lecturer.setPicture(null); // model mapper create new object if picture is empty, so have to make it null
            lecturer.setLinkedin(null);
            entityManager.persist(lecturer);
            LecturerTO lecturerTO = modelMapper.map(lecturer, LecturerTO.class);

            if (lecturerReqTO.getPicture() != null) {
                Picture picture = new Picture(lecturer, "lecturers/" + lecturer.getId());
                entityManager.persist(picture);
                Blob blobId = bucket.create(picture.getPicturePath(), lecturerReqTO.getPicture().getInputStream(), lecturerReqTO.getPicture().getContentType());
                lecturerTO.setPicturePath(blobId.signUrl(1, TimeUnit.DAYS, Storage.SignUrlOption.withV4Signature()).toString());
            }
            if (lecturerReqTO.getLinkedin() != null) {
                entityManager.persist(new LinkedIn(lecturer, lecturerReqTO.getLinkedin()));
                lecturerTO.setLinkedin(lecturerReqTO.getLinkedin());
            }

            entityManager.getTransaction().commit();
            return lecturerTO;
        }catch (IOException t){
            entityManager.getTransaction().rollback();
            throw new RuntimeException(t);
        }
    }

    @GetMapping(value = "/{lecturer-id}", produces = "application/json")
    public LecturerTO getLecturerDetails(@PathVariable("lecturer-id") Integer lecturerId){
        Lecturer lecturer = entityManager.find(Lecturer.class, lecturerId);
        if (lecturer == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lecturer not found" );
        LecturerTO lecturerTO = modelMapper.map(lecturer, LecturerTO.class);
        if (lecturer.getLinkedin() != null) lecturerTO.setLinkedin(lecturer.getLinkedin().getUrl());
        if (lecturer.getPicture() != null) {
            lecturerTO.setPicturePath(bucket.get(lecturer.getPicture().getPicturePath()).signUrl(1, TimeUnit.DAYS, Storage.SignUrlOption.withV4Signature()).toString());
        }
        return lecturerTO;
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(value = "/{lecturer-id}", consumes = "multipart/form-data")
    public void updateLecturerDetailsViaMultipart(@PathVariable("lecturer-id") Integer lecturerId
    , @ModelAttribute @Validated(LecturerReqTO.Update.class) LecturerReqTO lecturerReqTO){
        Lecturer currentLecturer = entityManager.find(Lecturer.class, lecturerId);
        if (currentLecturer == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lecturer Not Found");
        entityManager.getTransaction().begin();
        try {
            Lecturer newLecturer = modelMapper.map(lecturerReqTO, Lecturer.class);
            newLecturer.setId(lecturerId);
            newLecturer.setLinkedin(null);
            newLecturer.setPicture(null);

            if (lecturerReqTO.getPicture() != null && !lecturerReqTO.getPicture().isEmpty() ) {
                newLecturer.setPicture(new Picture(newLecturer, "lecturers/" + lecturerId));
            }

            if (lecturerReqTO.getLinkedin() != null) {
                newLecturer.setLinkedin(new LinkedIn(newLecturer, lecturerReqTO.getLinkedin()));
            }

            updateLinkedIn(currentLecturer, newLecturer);

            if (newLecturer.getPicture() != null && currentLecturer.getPicture() == null) {
                entityManager.persist(newLecturer.getPicture());
                bucket.create(newLecturer.getPicture().getPicturePath(), lecturerReqTO.getPicture().getInputStream(), lecturerReqTO.getPicture().getContentType());
            } else if (newLecturer.getPicture() == null && currentLecturer.getPicture() != null) {
                entityManager.remove(currentLecturer.getPicture());
                bucket.get(currentLecturer.getPicture().getPicturePath()).delete();
            } else if (newLecturer.getPicture() != null) {
                entityManager.merge(newLecturer.getPicture());
                bucket.create(newLecturer.getPicture().getPicturePath(), lecturerReqTO.getPicture().getInputStream(), lecturerReqTO.getPicture().getContentType());
            }

            entityManager.merge(newLecturer);
            entityManager.getTransaction().commit();
        } catch (Throwable e){
            entityManager.getTransaction().rollback();
            throw new RuntimeException(e);
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(value = "/{lecturer-id}", consumes = "application/json")
    public void updateLecturerDetailsViaJson(@PathVariable("lecturer-id") Integer lecturerId,
                                             @RequestBody @Validated LecturerTO lecturerUpdateReqTO){
        Lecturer currentLecturer = entityManager.find(Lecturer.class, lecturerId);
        if (currentLecturer == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lecturer Not Found");
        entityManager.getTransaction().begin();
        try{
            Lecturer newLecturer = modelMapper.map(lecturerUpdateReqTO, Lecturer.class);
            newLecturer.setId(lecturerId);
            newLecturer.setPicture(currentLecturer.getPicture());

            newLecturer.setLinkedin(lecturerUpdateReqTO.getLinkedin() == null ? currentLecturer.getLinkedin() : new LinkedIn(newLecturer,lecturerUpdateReqTO.getLinkedin()));

            updateLinkedIn(currentLecturer, newLecturer);

            entityManager.merge(newLecturer);
            entityManager.getTransaction().commit();
        } catch (Throwable t){
            entityManager.getTransaction().rollback();
            throw new RuntimeException(t);
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{lecturer-id}")
    public void deleteLecturer(@PathVariable("lecturer-id") Integer lecturerId){
        Lecturer lecturer = entityManager.find(Lecturer.class, lecturerId);
        if (lecturer == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lecturer not found");
        entityManager.getTransaction().begin();
        try{
            entityManager.remove(lecturer);
            if (lecturer.getPicture() != null) {
                bucket.get(lecturer.getPicture().getPicturePath()).delete();
            }
            entityManager.getTransaction().commit();
        } catch (Throwable t){
            entityManager.getTransaction().rollback();
            throw new RuntimeException(t);
        }
    }

    @GetMapping(produces = "application/json")
    public List<LecturerTO> getAllLecturers(){
        TypedQuery<Lecturer> query = entityManager.createQuery("SELECT l FROM Lecturer l", Lecturer.class);
        return getLecturerTOList(query);
    }

    @GetMapping(params = "type=full-time",produces = "application/json")
    public List<LecturerTO> getFullTimeLecturers(){
        TypedQuery<Lecturer> query = entityManager.createQuery("SELECT l FROM Lecturer l WHERE l.type = lk.ijse.dep13.eduforge.util.LecturerType.FULL_TIME", Lecturer.class);
        return getLecturerTOList(query);
    }

    @GetMapping(params = "type=visiting", produces = "application/json")
    public List<LecturerTO> getVisitingLecturers(){
        TypedQuery<Lecturer> query = entityManager.createQuery("SELECT l FROM Lecturer l WHERE l.type = lk.ijse.dep13.eduforge.util.LecturerType.VISITING", Lecturer.class);
        return getLecturerTOList(query);
    }

    private List<LecturerTO> getLecturerTOList(TypedQuery<Lecturer> query) {
        return query.getResultStream().map(lecturerEntity -> {
            LecturerTO lecturerTO = modelMapper.map(lecturerEntity, LecturerTO.class);
            if (lecturerEntity.getLinkedin() != null) lecturerTO.setLinkedin(lecturerEntity.getLinkedin().getUrl());
            if (lecturerEntity.getPicture() != null) {
                lecturerTO.setPicturePath(bucket.get(lecturerEntity.getPicture().getPicturePath()).signUrl(1, TimeUnit.DAYS, Storage.SignUrlOption.withV4Signature()).toString());
            }
            return lecturerTO;
        }).collect(Collectors.toList());
    }

    private void updateLinkedIn(Lecturer currentLecturer, Lecturer newLecturer) {
        if (currentLecturer.getLinkedin() != null && newLecturer.getLinkedin() == null) {
            entityManager.remove(currentLecturer.getLinkedin());
        } else if (currentLecturer.getLinkedin() == null && newLecturer.getLinkedin() != null) {
            entityManager.persist(newLecturer.getLinkedin());
        } else if(newLecturer.getLinkedin() != null){
            entityManager.merge(newLecturer.getLinkedin());
        }
    }
}
