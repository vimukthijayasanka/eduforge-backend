package lk.ijse.dep13.eduforge;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebRootConfig {

    @Bean(destroyMethod = "close")
    public EntityManagerFactory entityManagerFactory(){
       return Persistence.createEntityManagerFactory("default");
    }
}
