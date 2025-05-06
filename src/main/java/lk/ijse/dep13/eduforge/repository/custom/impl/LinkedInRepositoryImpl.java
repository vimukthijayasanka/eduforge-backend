package lk.ijse.dep13.eduforge.repository.custom.impl;

import lk.ijse.dep13.eduforge.entity.LinkedIn;
import lk.ijse.dep13.eduforge.repository.CrudRepositoryImpl;
import lk.ijse.dep13.eduforge.repository.custom.LinkedInRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class LinkedInRepositoryImpl extends CrudRepositoryImpl<LinkedIn, Integer> implements LinkedInRepository{

}
