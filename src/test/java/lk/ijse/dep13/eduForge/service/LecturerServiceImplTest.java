package lk.ijse.dep13.eduForge.service;

import com.google.cloud.storage.Bucket;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lk.ijse.dep13.eduforge.WebAppConfig;
import lk.ijse.dep13.eduforge.WebRootConfig;
import lk.ijse.dep13.eduforge.dto.request.LecturerReqTO;
import lk.ijse.dep13.eduforge.dto.response.LecturerTO;
import lk.ijse.dep13.eduforge.entity.Lecturer;
import lk.ijse.dep13.eduforge.entity.LinkedIn;
import lk.ijse.dep13.eduforge.exception.AppException;
import lk.ijse.dep13.eduforge.repository.custom.LecturerRepository;
import lk.ijse.dep13.eduforge.repository.custom.LinkedInRepository;
import lk.ijse.dep13.eduforge.repository.custom.PictureRepository;
import lk.ijse.dep13.eduforge.service.ServiceFactory;
import lk.ijse.dep13.eduforge.service.custom.LecturerService;
import lk.ijse.dep13.eduforge.store.AppStore;
import lk.ijse.dep13.eduforge.util.LecturerType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;
import static org.mockito.Mockito.*;

@SpringJUnitWebConfig(classes = {WebAppConfig.class, WebRootConfig.class})
//@ExtendWith(MockitoExtension.class)
public class LecturerServiceImplTest {

    private LecturerService lecturerService;
    @Autowired
    private EntityManagerFactory entityManagerFactory;
    @Autowired
    private Bucket bucket;

    private EntityManager entityManager;

//    @Mock
//    private LecturerRepository lecturerRepository;
//
//    @Mock
//    private LinkedInRepository linkedInRepository;
//
//    @Mock
//    private PictureRepository pictureRepository;

    @BeforeEach
    void setUp(){
        entityManager = entityManagerFactory.createEntityManager();
        // Here entity manager should be injected into AppStore before create LecturerService cuz its required a entity manager
        AppStore.setEntityManager(entityManager);
        AppStore.setBucket(bucket);
        lecturerService = ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.LECTURER);

//        when(lecturerRepository.save(any(Lecturer.class))).thenAnswer(inv ->{
//            Lecturer lecturer = inv.getArgument(0);
//            lecturer.setId(1);
//            return lecturer;
//        });
//
//        when(linkedInRepository.save(any(LinkedIn.class))).thenAnswer(inv -> inv.getArgument(0));
//
//        lecturerService.setLecturerRepository(lecturerRepository);
//        lecturerService.setLinkedInRepository(linkedInRepository);
//        lecturerService.setPictureRepository(pictureRepository);
    }

    @AfterEach
    void tearDown(){
        entityManager.close();
    }

    @Test
    void saveLecturer(){
        LecturerReqTO lecturerReqTO = new LecturerReqTO("Pedro Pascal", "Professor", "BSc, MSc, PHD", LecturerType.FULL_TIME, 0, null, "https://linkedin.com/pedro-pascal");
        LecturerTO lecturerTO = lecturerService.saveLecturer(lecturerReqTO);
        assertNotNull(lecturerTO.getId());
        assertTrue(lecturerTO.getId() > 0);
        assertEquals(lecturerReqTO.getName(), lecturerTO.getName());
        assertEquals(lecturerReqTO.getDesignation(), lecturerTO.getDesignation());
        assertEquals(lecturerReqTO.getQualification(), lecturerTO.getQualification());
        assertEquals(lecturerReqTO.getType(), lecturerTO.getType());
        assertEquals(lecturerReqTO.getDisplayOrder(), lecturerTO.getDisplayOrder());
        assumingThat(lecturerReqTO.getLinkedin() != null, ()-> assertEquals(lecturerReqTO.getLinkedin(), lecturerTO.getLinkedin()) );
        assumingThat(lecturerReqTO.getLinkedin() == null, ()-> assertNull(lecturerTO.getLinkedin()));
    }

    @Test
    void deleteLecturer(){
        LecturerReqTO lecturerReqTO = new LecturerReqTO("Pedro Pascal",
                "Professor",
                "BSc, MSc, PHD",
                LecturerType.FULL_TIME, 0, null, "https://linkedin.com/pedro-pascal");
        LecturerTO lecturerTO = lecturerService.saveLecturer(lecturerReqTO);
        lecturerService.deleteLecturer(lecturerTO.getId());
        assertThrows(AppException.class, ()-> lecturerService.getLecturerDetails(lecturerTO.getId()) );
        assertThrows(AppException.class, ()-> lecturerService.getLecturerDetails(-200) );
    }

    @Test
    void getLecturerDetails(){
        LecturerReqTO lecturerReqTO = new LecturerReqTO("Pedro Pascal",
                "Professor",
                "BSc, MSc, PHD",
                LecturerType.FULL_TIME, 0, null, "https://linkedin.com/pedro-pascal");
        LecturerTO lecturerTO = lecturerService.saveLecturer(lecturerReqTO);
        LecturerTO lecturerDetails = lecturerService.getLecturerDetails(lecturerTO.getId());
        assertEquals(lecturerTO, lecturerDetails);
        assertThrows(AppException.class, ()-> lecturerService.getLecturerDetails(-200) );
    }

    @Test
    void getAllLecturers(){
        for (int i = 0; i < 10; i++) {
            LecturerReqTO lecturerReqTO = new LecturerReqTO("Pedro Pascal",
                    "Professor",
                    "BSc, MSc, PHD",
                    i < 5 ? LecturerType.FULL_TIME : LecturerType.VISITING, 0, null, "https://linkedin.com/pedro-pascal");
            lecturerService.saveLecturer(lecturerReqTO);
        }
        assertTrue(lecturerService.getAllLecturers(null).size() >= 10);
        assertTrue(lecturerService.getAllLecturers(LecturerType.FULL_TIME).size() >= 5);
        assertTrue(lecturerService.getAllLecturers(LecturerType.VISITING).size() >= 5);

    }

    @Test
    void updateLecturerDetails(){
        LecturerReqTO lecturerReqTO = new LecturerReqTO("Pedro Pascal",
                "Professor",
                "BSc, MSc, PHD",
                LecturerType.FULL_TIME, 0, null, "https://linkedin.com/pedro-pascal");
        LecturerTO lecturerTO = lecturerService.saveLecturer(lecturerReqTO);
        lecturerTO.setLinkedin("https://linkedin.com/pedro-pascal-updated");
        lecturerTO.setDisplayOrder(10);
        lecturerService.updateLecturerDetails(lecturerTO);
        LecturerTO updatedLecturer = lecturerService.getLecturerDetails(lecturerTO.getId());
        assertEquals(lecturerTO, updatedLecturer);
    }
}
