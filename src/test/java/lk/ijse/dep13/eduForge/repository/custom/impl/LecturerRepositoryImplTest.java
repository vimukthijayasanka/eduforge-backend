package lk.ijse.dep13.eduForge.repository.custom.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import lk.ijse.dep13.eduforge.entity.Lecturer;
import lk.ijse.dep13.eduforge.repository.RepositoryFactory;
import lk.ijse.dep13.eduforge.repository.custom.LecturerRepository;
import lk.ijse.dep13.eduforge.repository.custom.impl.LecturerRepositoryImpl;
import lk.ijse.dep13.eduforge.util.LecturerType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LecturerRepositoryImplTest {

    private final LecturerRepository repository = RepositoryFactory.getInstance().getRepository(RepositoryFactory.RepositoryTypes.LECTURER);

    private EntityManager entityManager;

    @BeforeEach
    public void setUp() {
        entityManager = Persistence.createEntityManagerFactory("default").createEntityManager();
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
    }

    @Test
    void deleteById(){
    }

    @Test
    void existsById(){}

    @Test
    void findById(){}

    @Test
    void findAll(){}

    @Test
    void count(){}
}
