package lk.ijse.dep13.eduforge.exception;

import lombok.Getter;

@Getter
public class AppException extends RuntimeException {
    private final int errorCode;

    public AppException(int errorCode) {
        this.errorCode = errorCode;
    }

    public AppException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public AppException(String message, Throwable cause, int errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public AppException(Throwable cause, int errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }
}
