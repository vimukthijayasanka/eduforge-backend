package lk.ijse.dep13.eduForge.repository.custom.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lk.ijse.dep13.eduforge.WebRootConfig;
import lk.ijse.dep13.eduforge.entity.Lecturer;
import lk.ijse.dep13.eduforge.repository.RepositoryFactory;
import lk.ijse.dep13.eduforge.repository.custom.LecturerRepository;
import lk.ijse.dep13.eduforge.util.LecturerType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {WebRootConfig.class})
public class LecturerRepositoryImplTest {

    private final LecturerRepository repository = RepositoryFactory.getInstance().getRepository(RepositoryFactory.RepositoryTypes.LECTURER);

    private EntityManager entityManager;
    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @BeforeEach
    public void setUp() {
        entityManager = entityManagerFactory.createEntityManager();
        repository.setEntityManager(entityManager);
        entityManager.getTransaction().begin();
    }

    @AfterEach
    void tearDown() {
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Test
    void save(){
        Lecturer lecturer = new Lecturer("Joel Miller","Senior Lecturer","Bsc (Hons) in CS", LecturerType.FULL_TIME, 0);
        Lecturer savedLecturer = repository.save(lecturer);
        assertTrue(savedLecturer.getId() > 0);
        savedLecturer = entityManager.find(Lecturer.class, savedLecturer.getId());
        assertNotNull(savedLecturer);
    }

    @Test
    void update(){
        Lecturer lecturer = new Lecturer("Joel Miller","Senior Lecturer","Bsc (Hons) in CS", LecturerType.FULL_TIME, 0);
        Lecturer savedLecturer = repository.save(lecturer);
        savedLecturer.setName("Ellie Anderson");
        savedLecturer.setDesignation("Assistant Professor");
        savedLecturer.setQualification("Bsc (Hons) in SE");
        savedLecturer.setType(LecturerType.VISITING);
        repository.update(savedLecturer);

        Lecturer actualLecturer = entityManager.find(Lecturer.class, savedLecturer.getId());
        assertEquals(savedLecturer, actualLecturer);
    }

    @Test
    void deleteById(){
        Lecturer lecturer = new Lecturer("Joel Miller","Senior Lecturer","Bsc (Hons) in CS", LecturerType.FULL_TIME, 0);
        Lecturer savedLecturer = repository.save(lecturer);
        repository.deleteById(savedLecturer.getId());

        Lecturer dbLecturer = entityManager.find(Lecturer.class, savedLecturer.getId());
        assertNull(dbLecturer);
    }

    @Test
    void existsById(){
        Lecturer lecturer = new Lecturer("Joel Miller","Senior Lecturer","Bsc (Hons) in CS", LecturerType.FULL_TIME, 0);
        Lecturer savedLecturer = repository.save(lecturer);
        boolean result = repository.existsById(savedLecturer.getId());
        assertTrue(result);
    }

    @Test
    void findById(){
        Lecturer lecturer = new Lecturer("Joel Miller","Senior Lecturer","Bsc (Hons) in CS", LecturerType.FULL_TIME, 0);
        Lecturer savedLecturer = repository.save(lecturer);
        Optional<Lecturer> optionalLecturer1 = repository.findById(savedLecturer.getId());
        Optional<Lecturer> optionalLecturer2 = repository.findById(-200);
        assertTrue(optionalLecturer1.isPresent());
        assertTrue(optionalLecturer2.isEmpty());
    }

    @Test
    void findAll(){
        for (int i = 0; i < 8; i++) {
            Lecturer lecturer = new Lecturer("Joel Miller","Senior Lecturer","Bsc (Hons) in CS", LecturerType.FULL_TIME, 0);
            repository.save(lecturer);
        }
        List<Lecturer> allLecturers = repository.findAll();
        assertEquals(8, allLecturers.size());
        allLecturers.forEach(System.out::println);
    }

    @Test
    void count(){
        for (int i = 0; i < 10; i++) {
            Lecturer lecturer = new Lecturer("Joel Miller","Senior Lecturer","Bsc (Hons) in CS", LecturerType.FULL_TIME, 0);
            repository.save(lecturer);
        }
        long count = repository.count();
        assertTrue(count >= 120);
    }
}
