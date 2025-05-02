package lk.ijse.dep13.eduforge.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Constraint(validatedBy = LecturerProfileImageConstraintValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LecturerProfileImage {
    long maxFileSize() default 5 * 1024 * 1024;

    String message() default "{Invalid image file or file size exceeds the maximum file size of 5 MB}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
