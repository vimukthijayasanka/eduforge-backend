package lk.ijse.dep13.eduforge.repository;

import lk.ijse.dep13.eduforge.repository.custom.LecturerRepository;
import lk.ijse.dep13.eduforge.repository.custom.QueryRepository;
import lk.ijse.dep13.eduforge.repository.custom.impl.LecturerRepositoryImpl;
import lk.ijse.dep13.eduforge.repository.custom.impl.LinkedInRepositoryImpl;
import lk.ijse.dep13.eduforge.repository.custom.impl.PictureRepositoryImpl;

public class RepositoryFactory {

    public enum RepositoryTypes{
        LECTURER, LINKEDIN, PICTURE, QUERY;
    }

    private static RepositoryFactory INSTANCE;

    private RepositoryFactory() {
    }

    public static RepositoryFactory getInstance() {
        return (INSTANCE == null) ? INSTANCE = new RepositoryFactory() : INSTANCE;
    }

    public <T extends SuperRepository> T getRepository(RepositoryTypes type) {
        switch (type) {
            case LECTURER:
                return (T) new LecturerRepositoryImpl();
            case LINKEDIN:
                return (T) new LinkedInRepositoryImpl();
            case PICTURE:
                return (T) new PictureRepositoryImpl();
            case QUERY:
                return (T) new QueryRepositoryImpl();
            default:
                throw new IllegalArgumentException("Invalid Repository Type");
        }
    }
}
