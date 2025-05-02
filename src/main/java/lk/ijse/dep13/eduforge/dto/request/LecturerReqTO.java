package lk.ijse.dep13.eduforge.dto.request;

import lk.ijse.dep13.eduforge.util.LecturerType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LecturerReqTO implements Serializable {
    private String name;
    private String designation;
    private String qualification;
    private LecturerType type;
    private Integer displayOrder;
    private MultipartFile picture;
    private String linkedin;
}
