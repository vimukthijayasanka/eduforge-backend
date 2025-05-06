package lk.ijse.dep13.eduforge.controller;

import lk.ijse.dep13.eduforge.dto.request.LecturerReqTO;
import lk.ijse.dep13.eduforge.dto.response.LecturerTO;
import lk.ijse.dep13.eduforge.service.custom.LecturerService;
import lk.ijse.dep13.eduforge.util.LecturerType;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/lecturers")
@CrossOrigin
public class LecturerHttpController {

    private LecturerService lecturerService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = "multipart/form-data", produces = "application/json")
    public LecturerTO createNewLecturer(@ModelAttribute @Validated(LecturerReqTO.Create.class) LecturerReqTO lecturerReqTO) {
        return lecturerService.saveLecturer(lecturerReqTO);
    }

    @GetMapping(value = "/{lecturer-id}", produces = "application/json")
    public LecturerTO getLecturerDetails(@PathVariable("lecturer-id") Integer lecturerId) {
        return lecturerService.getLecturerDetails(lecturerId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(value = "/{lecturer-id}", consumes = "multipart/form-data")
    public void updateLecturerDetailsViaMultipart(@PathVariable("lecturer-id") Integer lecturerId
            , @ModelAttribute @Validated(LecturerReqTO.Update.class) LecturerReqTO lecturerReqTO) {
        lecturerReqTO.setId(lecturerId);
        lecturerService.updateLecturerDetails(lecturerReqTO);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(value = "/{lecturer-id}", consumes = "application/json")
    public void updateLecturerDetailsViaJson(@PathVariable("lecturer-id") Integer lecturerId,
                                             @RequestBody @Validated LecturerTO lecturerTO) {
        lecturerTO.setId(lecturerId);
        lecturerService.updateLecturerDetails(lecturerTO);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{lecturer-id}")
    public void deleteLecturer(@PathVariable("lecturer-id") Integer lecturerId) {
       lecturerService.deleteLecturer(lecturerId);
    }

    @GetMapping(produces = "application/json")
    public List<LecturerTO> getAllLecturers() {
        return lecturerService.getAllLecturers(null);
    }

    @GetMapping(params = "type=full-time", produces = "application/json")
    public List<LecturerTO> getFullTimeLecturers() {
        return lecturerService.getAllLecturers(LecturerType.FULL_TIME);
    }

    @GetMapping(params = "type=visiting", produces = "application/json")
    public List<LecturerTO> getVisitingLecturers() {
        return lecturerService.getAllLecturers(LecturerType.VISITING);
    }
}
