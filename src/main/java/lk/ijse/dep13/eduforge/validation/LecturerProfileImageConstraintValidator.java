package lk.ijse.dep13.eduforge.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class LecturerProfileImageConstraintValidator implements ConstraintValidator<LecturerProfileImage, MultipartFile> {

    private long maximumFileSize;

    @Override
    public void initialize(LecturerProfileImage constraintAnnotation) {
        maximumFileSize = constraintAnnotation.maxFileSize();
    }

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext constraintValidatorContext) {
       if (multipartFile == null || multipartFile.isEmpty()) return true;
       if (multipartFile.getContentType() == null || !multipartFile.getContentType().startsWith("image/")) return false;
       if (multipartFile.getSize() > maximumFileSize) return false;
       return true;
    }
}
