package lk.ijse.dep13.eduforge.store;

import com.google.cloud.storage.Bucket;
import jakarta.persistence.EntityManager;
import org.modelmapper.ModelMapper;

public class AppStore {
    private static final ThreadLocal<EntityManager> emStore = new ThreadLocal<>();

    private static final ThreadLocal<Bucket> bucketStore = new ThreadLocal<>();

    public static EntityManager getEntityManager() {
        return emStore.get();
    }

    public static void setEntityManager(EntityManager entityManager) {
        emStore.set(entityManager);
    }

    public static Bucket getBucket() {
        return bucketStore.get();
    }

    public static void setBucket(Bucket bucket) {
        bucketStore.set(bucket);
    }
}
