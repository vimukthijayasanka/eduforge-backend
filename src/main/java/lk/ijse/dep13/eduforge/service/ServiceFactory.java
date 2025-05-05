package lk.ijse.dep13.eduforge.service;

import lk.ijse.dep13.eduforge.service.impl.LecturerServiceImpl;

public class ServiceFactory {
    private static ServiceFactory INSTANCE;

    public enum ServiceType{
        LECTURER, USER
    }

    private ServiceFactory(){}

    public static ServiceFactory getInstance() {
        return (INSTANCE == null) ? (INSTANCE = new ServiceFactory()) : INSTANCE;
    }

    public <T extends SuperService> T getService(ServiceType type){
        switch (type){
            case LECTURER:
                return (T) new LecturerServiceImpl();
            case USER:
                throw new RuntimeException("Not Implemented yet");
            default:
                throw new IllegalArgumentException("Invalid Service Type");
        }
    }
}
