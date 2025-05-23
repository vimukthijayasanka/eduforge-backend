package lk.ijse.dep13.eduforge.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lk.ijse.dep13.eduforge.entity.Lecturer;
import lk.ijse.dep13.eduforge.util.LecturerType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class LecturerRepositoryImplTest {

    @Autowired
    private LecturerRepository repository;

    @PersistenceContext
    private EntityManager entityManager;

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
        repository.save(savedLecturer);

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
        assertTrue(allLecturers.size() >= 8);
        allLecturers.forEach(System.out::println);
    }

    @Test
    void count(){
        for (int i = 0; i < 10; i++) {
            Lecturer lecturer = new Lecturer("Joel Miller","Senior Lecturer","Bsc (Hons) in CS", LecturerType.FULL_TIME, 0);
            repository.save(lecturer);
        }
        long count = repository.count();
        assertTrue(count >= 10);
    }
}
