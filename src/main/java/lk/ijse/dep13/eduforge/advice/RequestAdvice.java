package lk.ijse.dep13.eduforge.advice;

import com.google.cloud.storage.Bucket;
import jakarta.persistence.EntityManager;
import lk.ijse.dep13.eduforge.service.ServiceFactory;
import lk.ijse.dep13.eduforge.store.AppStore;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Aspect
@Component
public class RequestAdvice {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private Bucket bucket;

    @Before("within(lk.ijse.dep13.eduforge.controller.*) &&" +
            " @target(org.springframework.web.bind.annotation.RestController)")
    public void interceptHandlerMethods(JoinPoint jp) throws NoSuchFieldException, IllegalAccessException {
        AppStore.setBucket(bucket);
        AppStore.setEntityManager(entityManager);
        Field field = jp.getTarget().getClass().getDeclaredField("lecturerService");
        field.setAccessible(true);
        field.set(jp.getTarget(), ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.LECTURER));
    }
}
