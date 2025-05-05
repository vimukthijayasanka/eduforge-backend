package lk.ijse.dep13.eduforge.service.impl;

import lk.ijse.dep13.eduforge.dto.request.LecturerReqTO;
import lk.ijse.dep13.eduforge.dto.response.LecturerTO;
import lk.ijse.dep13.eduforge.repository.RepositoryFactory;
import lk.ijse.dep13.eduforge.repository.custom.LecturerRepository;
import lk.ijse.dep13.eduforge.repository.custom.LinkedInRepository;
import lk.ijse.dep13.eduforge.repository.custom.PictureRepository;
import lk.ijse.dep13.eduforge.service.custom.LecturerService;
import lk.ijse.dep13.eduforge.store.AppStore;
import lk.ijse.dep13.eduforge.util.LecturerType;

import java.util.List;

public class LecturerServiceImpl implements LecturerService {

    private final LecturerRepository lecturerRepository = RepositoryFactory.getInstance().getRepository(RepositoryFactory.RepositoryTypes.LECTURER);

    private final LinkedInRepository linkedInRepository = RepositoryFactory.getInstance().getRepository(RepositoryFactory.RepositoryTypes.LINKEDIN);

    private final PictureRepository pictureRepository = RepositoryFactory.getInstance().getRepository(RepositoryFactory.RepositoryTypes.PICTURE);

    public LecturerServiceImpl() {
        lecturerRepository.setEntityManager(AppStore.getEntityManager());
        linkedInRepository.setEntityManager(AppStore.getEntityManager());
        pictureRepository.setEntityManager(AppStore.getEntityManager());
    }

    @Override
    public LecturerTO saveLecturer(LecturerReqTO lecturerReqTO) {
        AppStore.getEntityManager().getTransaction().begin();
        try{
            AppStore.getEntityManager().getTransaction().commit();
            return null;
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
