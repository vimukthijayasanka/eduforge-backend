package lk.ijse.dep13.eduforge.service.custom.impl;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import jakarta.transaction.Transactional;
import lk.ijse.dep13.eduforge.dto.request.LecturerReqTO;
import lk.ijse.dep13.eduforge.dto.response.LecturerTO;
import lk.ijse.dep13.eduforge.entity.Lecturer;
import lk.ijse.dep13.eduforge.entity.LinkedIn;
import lk.ijse.dep13.eduforge.entity.Picture;
import lk.ijse.dep13.eduforge.exception.AppException;
import lk.ijse.dep13.eduforge.repository.LecturerRepository;
import lk.ijse.dep13.eduforge.repository.LinkedInRepository;
import lk.ijse.dep13.eduforge.repository.PictureRepository;
import lk.ijse.dep13.eduforge.service.custom.LecturerService;
import lk.ijse.dep13.eduforge.util.LecturerType;
import lk.ijse.dep13.eduforge.service.util.Transformer;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Transactional
public class LecturerServiceImpl implements LecturerService {

    private final LecturerRepository lecturerRepository;
    private final LinkedInRepository linkedInRepository;
    private final PictureRepository pictureRepository;
    private final Transformer transformer;
    private final Bucket bucket;

    public LecturerServiceImpl(LecturerRepository lecturerRepository, LinkedInRepository linkedInRepository, PictureRepository pictureRepository, Transformer transformer, Bucket bucket) {
        this.lecturerRepository = lecturerRepository;
        this.linkedInRepository = linkedInRepository;
        this.pictureRepository = pictureRepository;
        this.transformer = transformer;
        this.bucket = bucket;
    }

    @Override
    public LecturerTO saveLecturer(LecturerReqTO lecturerReqTO) {
        Lecturer lecturer = transformer.fromLecturerReqTO(lecturerReqTO);
        lecturerRepository.save(lecturer);

        if (lecturerReqTO.getLinkedin() != null) {
                linkedInRepository.save(lecturer.getLinkedin());
        }

            String signUrl = null;
            if (lecturerReqTO.getPicture() != null){
                Picture picture = new Picture(lecturer, "lecturers/" + lecturer.getId());
                pictureRepository.save(picture);

                Blob blobId = null;
                try {
                    blobId = bucket.create(picture.getPicturePath(),
                            lecturerReqTO.getPicture().getInputStream(),
                            lecturerReqTO.getPicture().getContentType());
                } catch (IOException e){
                    throw new AppException("Failed to upload the image", e, 500);
            }
                signUrl = (blobId.signUrl(1, TimeUnit.DAYS, Storage.SignUrlOption.withV4Signature()).toString());
        }
            LecturerTO lecturerTO = transformer.toLecturerTO(lecturer);
            lecturerTO.setPicturePath(signUrl);
            return lecturerTO;
    }

    @Override
    public void updateLecturerDetails(LecturerReqTO lecturerReqTO) {
        Optional<Lecturer> optLecturer = lecturerRepository.findById(lecturerReqTO.getId());
        if (optLecturer.isEmpty()) throw new AppException("Lecturer not found", 404);
        Lecturer currentLecturer = optLecturer.get();

        Lecturer newLecturer = transformer.fromLecturerReqTO(lecturerReqTO);
        if (lecturerReqTO.getPicture() != null) {
            newLecturer.setPicture(new Picture(newLecturer, "lecturers/" + currentLecturer.getId()));
        }
        if (lecturerReqTO.getLinkedin() != null) {
            newLecturer.setLinkedin(new LinkedIn(newLecturer, lecturerReqTO.getLinkedin()));
        }

        updateLinkedIn(currentLecturer, newLecturer);

        try{
            if (newLecturer.getPicture() != null && currentLecturer.getPicture() == null) {
                pictureRepository.save(newLecturer.getPicture());
                bucket.create(newLecturer.getPicture().getPicturePath(), lecturerReqTO.getPicture().getInputStream(), lecturerReqTO.getPicture().getContentType());
            } else if (newLecturer.getPicture() == null && currentLecturer.getPicture() != null) {
                pictureRepository.deleteById(currentLecturer.getId());
                bucket.get(currentLecturer.getPicture().getPicturePath()).delete();
            } else if (newLecturer.getPicture() != null) {
                pictureRepository.save(newLecturer.getPicture());
                bucket.create(newLecturer.getPicture().getPicturePath(), lecturerReqTO.getPicture().getInputStream(), lecturerReqTO.getPicture().getContentType());
            }
        }catch (IOException e){
            throw new AppException("Failed to update the lecturer details", e, 500);
        }
        lecturerRepository.save(newLecturer);
    }

    @Override
    public void updateLecturerDetails(LecturerTO lecturerTO) {
        Optional<Lecturer> optLecturer = lecturerRepository.findById(lecturerTO.getId());
        if (optLecturer.isEmpty()) throw new AppException("Lecturer not found", 404);
        Lecturer currentLecturer = optLecturer.get();

        Lecturer newLecturer = transformer.fromLecturerTO(lecturerTO);
        newLecturer.setPicture(currentLecturer.getPicture());
        updateLinkedIn(currentLecturer, newLecturer);
        lecturerRepository.save(newLecturer);
    }

    @Override
    public void deleteLecturer(Integer lecturerId) {
        if (!lecturerRepository.existsById(lecturerId)) throw new AppException("Lecturer not found", 404);
        lecturerRepository.deleteById(lecturerId);
    }

    @Override
    public LecturerTO getLecturerDetails(Integer lecturerId) {
        Optional<Lecturer> optLecturer = lecturerRepository.findById(lecturerId);
        if (optLecturer.isEmpty()) throw new AppException("Lecturer not found", 404);
        LecturerTO lecturerTO = transformer.toLecturerTO(optLecturer.get());
        if (optLecturer.get().getPicture() != null) {
            lecturerTO.setPicturePath(bucket.get(optLecturer.get().getPicture().getPicturePath()).signUrl(1, TimeUnit.DAYS, Storage.SignUrlOption.withV4Signature()).toString());
        }
        return lecturerTO;
    }

    @Override
    public List<LecturerTO> getAllLecturers(LecturerType lecturerType) {
        List<Lecturer> lecturerList;
        if (lecturerType == LecturerType.FULL_TIME) {
            lecturerList = lecturerRepository.findFullTimeLecturers();
        } else if (lecturerType == LecturerType.VISITING) {
            lecturerList = lecturerRepository.findVisitingLecturers();
        } else {
            lecturerList = lecturerRepository.findAll();
        }
        return lecturerList.stream().map(lecturer -> {
            LecturerTO lecturerTO = transformer.toLecturerTO(lecturer);
            if (lecturer.getPicture() != null) {
                lecturerTO.setPicturePath(bucket.get(lecturer.getPicture().getPicturePath()).signUrl(1, TimeUnit.DAYS, Storage.SignUrlOption.withV4Signature()).toString());
            }
            return lecturerTO;
        }).collect(Collectors.toList());
    }

    private void updateLinkedIn(Lecturer currentLecturer, Lecturer newLecturer) {
        if (currentLecturer.getLinkedin() != null && newLecturer.getLinkedin() == null) {
            linkedInRepository.deleteById(currentLecturer.getId());
        } else if (currentLecturer.getLinkedin() == null && newLecturer.getLinkedin() != null) {
            linkedInRepository.save(newLecturer.getLinkedin());
        } else if(newLecturer.getLinkedin() != null){
            linkedInRepository.save(newLecturer.getLinkedin());
        }
    }
}
