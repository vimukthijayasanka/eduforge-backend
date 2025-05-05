package lk.ijse.dep13.eduforge.service.impl;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;
import lk.ijse.dep13.eduforge.dto.request.LecturerReqTO;
import lk.ijse.dep13.eduforge.dto.response.LecturerTO;
import lk.ijse.dep13.eduforge.entity.Lecturer;
import lk.ijse.dep13.eduforge.entity.LinkedIn;
import lk.ijse.dep13.eduforge.entity.Picture;
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
import java.util.concurrent.TimeUnit;

public class LecturerServiceImpl implements LecturerService {

    private final LecturerRepository lecturerRepository = RepositoryFactory.getInstance().getRepository(RepositoryFactory.RepositoryTypes.LECTURER);

    private final LinkedInRepository linkedInRepository = RepositoryFactory.getInstance().getRepository(RepositoryFactory.RepositoryTypes.LINKEDIN);

    private final PictureRepository pictureRepository = RepositoryFactory.getInstance().getRepository(RepositoryFactory.RepositoryTypes.PICTURE);

    private final Transformer transformer = new Transformer();

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
            lecturer = lecturerRepository.save(lecturer);

            if (lecturerReqTO.getLinkedin() != null) {
                linkedInRepository.save(new LinkedIn(lecturer, lecturerReqTO.getLinkedin()));
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
        } catch (Exception e) {
            AppStore.getEntityManager().getTransaction().rollback();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateLecturerDetailsWithImage(LecturerReqTO lecturerReqTO) {
        AppStore.getEntityManager().getTransaction().begin();
        try{
            AppStore.getEntityManager().getTransaction().commit();
        }catch (Exception e){
            AppStore.getEntityManager().getTransaction().rollback();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateLecturerDetailsWithoutImage(LecturerReqTO lecturerReqTO) {
        AppStore.getEntityManager().getTransaction().begin();
        try{
            AppStore.getEntityManager().getTransaction().commit();
        }catch (Exception e){
            AppStore.getEntityManager().getTransaction().rollback();
            throw new RuntimeException(e);
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
}
