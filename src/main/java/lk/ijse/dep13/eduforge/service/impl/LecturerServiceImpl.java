package lk.ijse.dep13.eduforge.service.impl;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;
import lk.ijse.dep13.eduforge.dto.request.LecturerReqTO;
import lk.ijse.dep13.eduforge.dto.response.LecturerTO;
import lk.ijse.dep13.eduforge.entity.Lecturer;
import lk.ijse.dep13.eduforge.entity.LinkedIn;
import lk.ijse.dep13.eduforge.entity.Picture;
import lk.ijse.dep13.eduforge.exception.AppException;
import lk.ijse.dep13.eduforge.repository.RepositoryFactory;
import lk.ijse.dep13.eduforge.repository.custom.LecturerRepository;
import lk.ijse.dep13.eduforge.repository.custom.LinkedInRepository;
import lk.ijse.dep13.eduforge.repository.custom.PictureRepository;
import lk.ijse.dep13.eduforge.service.custom.LecturerService;
import lk.ijse.dep13.eduforge.store.AppStore;
import lk.ijse.dep13.eduforge.util.LecturerType;
import lk.ijse.dep13.eduforge.util.Transformer;

import javax.xml.transform.TransformerFactory;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class LecturerServiceImpl implements LecturerService {

    private final LecturerRepository lecturerRepository = RepositoryFactory.getInstance().getRepository(RepositoryFactory.RepositoryTypes.LECTURER);
    private final LinkedInRepository linkedInRepository = RepositoryFactory.getInstance().getRepository(RepositoryFactory.RepositoryTypes.LINKEDIN);
    private final PictureRepository pictureRepository = RepositoryFactory.getInstance().getRepository(RepositoryFactory.RepositoryTypes.PICTURE);

//    public void setLecturerRepository(LecturerRepository lecturerRepository) {
//        this.lecturerRepository = lecturerRepository;
//                lecturerRepository.setEntityManager(AppStore.getEntityManager());
//
//    }
//
//    public void setLinkedInRepository(LinkedInRepository linkedInRepository) {
//        this.linkedInRepository = linkedInRepository;
//        linkedInRepository.setEntityManager(AppStore.getEntityManager());
//    }
//
//    public void setPictureRepository(PictureRepository pictureRepository) {
//        this.pictureRepository = pictureRepository;
//        pictureRepository.setEntityManager(AppStore.getEntityManager());
//    }

    private Transformer transformer = new Transformer();

    public LecturerServiceImpl() {
        lecturerRepository.setEntityManager(AppStore.getEntityManager());
        linkedInRepository.setEntityManager(AppStore.getEntityManager());
        pictureRepository.setEntityManager(AppStore.getEntityManager());
    }

    @Override
    public LecturerTO saveLecturer(LecturerReqTO lecturerReqTO) {
        AppStore.getEntityManager().getTransaction().begin();
        try{
            Lecturer lecturer = transformer.fromLecturerReqTO(lecturerReqTO);
            lecturerRepository.save(lecturer);

            if (lecturerReqTO.getLinkedin() != null) {
                linkedInRepository.save(lecturer.getLinkedin());
            }

            String signUrl = null;
            if (lecturerReqTO.getPicture() != null){
                Picture picture = new Picture(lecturer, "lecturers/" + lecturer.getId());
                pictureRepository.save(picture);

                Blob blobId = AppStore.getBucket().create(picture.getPicturePath(),
                        lecturerReqTO.getPicture().getInputStream(),
                        lecturerReqTO.getPicture().getContentType());

                signUrl = (blobId.signUrl(1, TimeUnit.DAYS, Storage.SignUrlOption.withV4Signature()).toString());
            }

            AppStore.getEntityManager().getTransaction().commit();
            LecturerTO lecturerTO = transformer.toLecturerTO(lecturer);
            lecturerTO.setPicturePath(signUrl);
            return lecturerTO;
        } catch (Throwable e) {
            AppStore.getEntityManager().getTransaction().rollback();
            throw new AppException("Failed to save the lecturer", e, 500);
        }
    }

    @Override
    public void updateLecturerDetails(LecturerReqTO lecturerReqTO) {
        Optional<Lecturer> optLecturer = lecturerRepository.findById(lecturerReqTO.getId());
        if (optLecturer.isEmpty()) throw new AppException("Lecturer not found", 404);
        Lecturer currentLecturer = optLecturer.get();

        AppStore.getEntityManager().getTransaction().begin();
        try{
            Lecturer newLecturer = transformer.fromLecturerReqTO(lecturerReqTO);
            if (lecturerReqTO.getPicture() != null) {
                newLecturer.setPicture(new Picture(newLecturer, "lecturers/" + currentLecturer.getId()));
            }
            if (lecturerReqTO.getLinkedin() != null) {
                newLecturer.setLinkedin(new LinkedIn(newLecturer, lecturerReqTO.getLinkedin()));
            }
            updateLinkedIn(currentLecturer, newLecturer);

            if (newLecturer.getPicture() != null && currentLecturer.getPicture() == null) {
                pictureRepository.save(newLecturer.getPicture());
                AppStore.getBucket().create(newLecturer.getPicture().getPicturePath(), lecturerReqTO.getPicture().getInputStream(), lecturerReqTO.getPicture().getContentType());
            } else if (newLecturer.getPicture() == null && currentLecturer.getPicture() != null) {
                pictureRepository.deleteById(currentLecturer.getId());
                AppStore.getBucket().get(currentLecturer.getPicture().getPicturePath()).delete();
            } else if (newLecturer.getPicture() != null) {
                pictureRepository.update(newLecturer.getPicture());
                AppStore.getBucket().create(newLecturer.getPicture().getPicturePath(), lecturerReqTO.getPicture().getInputStream(), lecturerReqTO.getPicture().getContentType());
            }

            lecturerRepository.update(newLecturer);
            AppStore.getEntityManager().getTransaction().commit();
        }catch (Exception e){
            AppStore.getEntityManager().getTransaction().rollback();
            throw new AppException("Failed to update the lecturer details", e, 500);
        }
    }

    @Override
    public void updateLecturerDetails(LecturerTO lecturerTO) {
        Optional<Lecturer> optLecturer = lecturerRepository.findById(lecturerTO.getId());
        if (optLecturer.isEmpty()) throw new AppException("Lecturer not found", 404);
        Lecturer currentLecturer = optLecturer.get();

        AppStore.getEntityManager().getTransaction().begin();
        try{
            Lecturer newLecturer = transformer.fromLecturerTO(lecturerTO);
            newLecturer.setPicture(currentLecturer.getPicture());
            updateLinkedIn(currentLecturer, newLecturer);
            lecturerRepository.update(newLecturer);
            AppStore.getEntityManager().getTransaction().commit();
        }catch (Exception e){
            AppStore.getEntityManager().getTransaction().rollback();
            throw new AppException("Failed to update the lecturer details", e, 500);
        }
    }

    @Override
    public void deleteLecturer(Integer lecturerId) {
        AppStore.getEntityManager().getTransaction().begin();
        try{
            AppStore.getEntityManager().getTransaction().commit();
        }catch (Exception e){
            AppStore.getEntityManager().getTransaction().rollback();
            throw new RuntimeException(e);
        }
    }

    @Override
    public LecturerTO getLecturerDetails(Integer lecturerId) {
        AppStore.getEntityManager().getTransaction().begin();
        try{
            AppStore.getEntityManager().getTransaction().commit();
            return null;
        }catch (Exception e){
            AppStore.getEntityManager().getTransaction().rollback();
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<LecturerTO> getAllLecturers(LecturerType lecturerType) {
        AppStore.getEntityManager().getTransaction().begin();
        try{
            AppStore.getEntityManager().getTransaction().commit();
            return List.of();
        }catch (Exception e){
            AppStore.getEntityManager().getTransaction().rollback();
            throw new RuntimeException(e);
        }
    }

    private void updateLinkedIn(Lecturer currentLecturer, Lecturer newLecturer) {
        if (currentLecturer.getLinkedin() != null && newLecturer.getLinkedin() == null) {
            linkedInRepository.deleteById(currentLecturer.getId());
        } else if (currentLecturer.getLinkedin() == null && newLecturer.getLinkedin() != null) {
            linkedInRepository.save(newLecturer.getLinkedin());
        } else if(newLecturer.getLinkedin() != null){
            linkedInRepository.update(newLecturer.getLinkedin());
        }
    }
}
