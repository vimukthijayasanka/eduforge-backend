package lk.ijse.dep13.eduforge.service.custom.impl;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import jakarta.transaction.Transactional;
import lk.ijse.dep13.eduforge.dto.request.LecturerReqTO;
import lk.ijse.dep13.eduforge.dto.LecturerTO;
import lk.ijse.dep13.eduforge.entity.Lecturer;
import lk.ijse.dep13.eduforge.entity.LinkedIn;
import lk.ijse.dep13.eduforge.entity.Picture;
import lk.ijse.dep13.eduforge.exception.AppException;
import lk.ijse.dep13.eduforge.repository.LecturerRepository;
import lk.ijse.dep13.eduforge.repository.LinkedInRepository;
import lk.ijse.dep13.eduforge.repository.PictureRepository;
import lk.ijse.dep13.eduforge.service.LecturerService;
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
        Lecturer currentLecturer = lecturerRepository.findById(lecturerReqTO.getId()).orElseThrow(() -> new AppException("Lecturer not found", 404));

        Blob blobId = null;
        if (currentLecturer.getPicture() != null) {
            blobId = bucket.get(currentLecturer.getPicture().getPicturePath());
            pictureRepository.delete(currentLecturer.getPicture());
        }

        if (currentLecturer.getLinkedin() != null) {
            linkedInRepository.delete(currentLecturer.getLinkedin());
        }

        Lecturer newLecturer = transformer.fromLecturerReqTO(lecturerReqTO);
        newLecturer.setLinkedin(null);
        newLecturer = lecturerRepository.save(newLecturer);

        if (lecturerReqTO.getPicture() != null) {
            Picture picture = new Picture(newLecturer, "lecturers/" + newLecturer.getId());
            newLecturer.setPicture(pictureRepository.save(picture));
        }
        if (lecturerReqTO.getLinkedin() != null) {
            LinkedIn linkedIn = new LinkedIn(newLecturer, lecturerReqTO.getLinkedin());
            newLecturer.setLinkedin(linkedInRepository.save(linkedIn));
        }

        try{
            if (lecturerReqTO.getPicture() != null) {
                bucket.create(newLecturer.getPicture().getPicturePath(), lecturerReqTO.getPicture().getInputStream(), lecturerReqTO.getPicture().getContentType());
            } else if (blobId != null){
                blobId.delete();
            }
        }catch (IOException e){
            throw new AppException("Failed to update the lecturer details", e, 500);
        }
    }

    @Override
    public void updateLecturerDetails(LecturerTO lecturerTO) {
        Lecturer currentLecturer = lecturerRepository.findById(lecturerTO.getId()).orElseThrow(() -> new AppException("Lecturer not found", 404));

        if (currentLecturer.getLinkedin() != null) {
            linkedInRepository.delete(currentLecturer.getLinkedin());
        }
        Lecturer newLecturer = transformer.fromLecturerTO(lecturerTO);
        newLecturer.setLinkedin(null);

        newLecturer = lecturerRepository.save(newLecturer);
        if (lecturerTO.getLinkedin() != null) {
            LinkedIn linkedIn = new LinkedIn(newLecturer, lecturerTO.getLinkedin());
            newLecturer.setLinkedin(linkedInRepository.save(linkedIn));
        }
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
        if (lecturerType != null) {
            lecturerList = lecturerRepository.findLecturersByType(lecturerType);
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
