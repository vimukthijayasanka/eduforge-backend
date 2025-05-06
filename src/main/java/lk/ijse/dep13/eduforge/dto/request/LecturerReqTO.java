package lk.ijse.dep13.eduforge.dto.request;

import jakarta.validation.constraints.*;
import jakarta.validation.groups.Default;
import lk.ijse.dep13.eduforge.util.LecturerType;
import lk.ijse.dep13.eduforge.validation.LecturerProfileImage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LecturerReqTO implements Serializable {
    private Integer id;
    @NotBlank(message = "Name can't be empty")
    @Pattern(regexp = "^[A-Za-z ]{2,}$", message = "Invalid name")
    private String name;
    @NotBlank(message = "Designation can't be empty")
    @Length(min = 3, message = "Invalid designation")
    private String designation;
    @NotBlank(message = "Qualification can't be empty")
    @Length(min = 3, message = "Invalid qualification")
    private String qualification;
    @NotNull(message = "Type should be either full-time or visiting")
    private LecturerType type;
    @Null(groups = Create.class, message = "Display order should be empty")
    @NotNull(groups = Update.class, message = "Display order can't be empty")
    @PositiveOrZero(groups = Update.class, message = "Invalid display order")
    private Integer displayOrder;
    @LecturerProfileImage
    private MultipartFile picture;
    @Pattern(regexp = "^http(s)://.+$", message = "Invalid linkedIn URL")
    private String linkedin;

    public interface Create extends Default {}
    public interface Update extends Default {}
}
