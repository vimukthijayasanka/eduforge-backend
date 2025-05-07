package lk.ijse.dep13.eduforge.repository;

import lk.ijse.dep13.eduforge.entity.LinkedIn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkedInRepository extends JpaRepository<LinkedIn, Integer> {

}
