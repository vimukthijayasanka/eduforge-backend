package lk.ijse.dep13.eduforge;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.RequestScope;

@Configuration
public class WebRootConfig {

    @Bean(destroyMethod = "close")
    public EntityManagerFactory entityManagerFactory(){
       return Persistence.createEntityManagerFactory("default");
    }

    @Bean(destroyMethod = "close")
    @RequestScope // here when request comes initially creating a PROXY when request gone this close
    public EntityManager entityManager(){
        return entityManagerFactory().createEntityManager();
    }

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
