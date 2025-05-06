package lk.ijse.dep13.eduforge.advice;

import lk.ijse.dep13.eduforge.exception.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public void handleAppWideException(AppException appException){
        ResponseStatusException resExp;
        if (appException.getErrorCode() == 404){
            resExp = new ResponseStatusException(HttpStatus.NOT_FOUND, appException.getMessage());
        } else{
            resExp = new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, appException.getMessage());
        }
        appException.initCause(resExp);
        throw resExp;
    }
}
