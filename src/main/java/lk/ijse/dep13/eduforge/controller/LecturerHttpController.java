package lk.ijse.dep13.eduforge.controller;

import com.google.cloud.storage.Bucket;
import jakarta.persistence.EntityManager;
import lk.ijse.dep13.eduforge.dto.request.LecturerReqTO;
import lk.ijse.dep13.eduforge.dto.response.LecturerTO;
import lk.ijse.dep13.eduforge.service.ServiceFactory;
import lk.ijse.dep13.eduforge.service.custom.LecturerService;
import lk.ijse.dep13.eduforge.store.AppStore;
import lk.ijse.dep13.eduforge.util.LecturerType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/lecturers")
@CrossOrigin
public class LecturerHttpController {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private Bucket bucket;

    private final LecturerService lecturerService = ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.LECTURER);


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = "multipart/form-data", produces = "application/json")
    public LecturerTO createNewLecturer(@ModelAttribute @Validated(LecturerReqTO.Create.class) LecturerReqTO lecturerReqTO) {
        AppStore.setEntityManager(entityManager);
        AppStore.setBucket(bucket);
        return lecturerService.saveLecturer(lecturerReqTO);
    }

    @GetMapping(value = "/{lecturer-id}", produces = "application/json")
    public LecturerTO getLecturerDetails(@PathVariable("lecturer-id") Integer lecturerId) {
        AppStore.setEntityManager(entityManager);
        AppStore.setBucket(bucket);
        return lecturerService.getLecturerDetails(lecturerId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(value = "/{lecturer-id}", consumes = "multipart/form-data")
    public void updateLecturerDetailsViaMultipart(@PathVariable("lecturer-id") Integer lecturerId
            , @ModelAttribute @Validated(LecturerReqTO.Update.class) LecturerReqTO lecturerReqTO) {
        AppStore.setEntityManager(entityManager);
        AppStore.setBucket(bucket);
        lecturerReqTO.setId(lecturerId);
        lecturerService.updateLecturerDetails(lecturerReqTO);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(value = "/{lecturer-id}", consumes = "application/json")
    public void updateLecturerDetailsViaJson(@PathVariable("lecturer-id") Integer lecturerId,
                                             @RequestBody @Validated LecturerTO lecturerTO) {
        AppStore.setEntityManager(entityManager);
        AppStore.setBucket(bucket);
        lecturerTO.setId(lecturerId);
        lecturerService.updateLecturerDetails(lecturerTO);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{lecturer-id}")
    public void deleteLecturer(@PathVariable("lecturer-id") Integer lecturerId) {
        AppStore.setEntityManager(entityManager);
        AppStore.setBucket(bucket);
        lecturerService.deleteLecturer(lecturerId);
    }

    @GetMapping(produces = "application/json")
    public List<LecturerTO> getAllLecturers() {
        AppStore.setEntityManager(entityManager);
        AppStore.setBucket(bucket);
        return lecturerService.getAllLecturers(null);
    }

    @GetMapping(params = "type=full-time", produces = "application/json")
    public List<LecturerTO> getFullTimeLecturers() {
        AppStore.setEntityManager(entityManager);
        AppStore.setBucket(bucket);
        return lecturerService.getAllLecturers(LecturerType.FULL_TIME);
    }

    @GetMapping(params = "type=visiting", produces = "application/json")
    public List<LecturerTO> getVisitingLecturers() {
        AppStore.setEntityManager(entityManager);
        AppStore.setBucket(bucket);
        return lecturerService.getAllLecturers(LecturerType.VISITING);
    }
}
