package lk.ijse.dep13.eduforge.dto.response;

import lk.ijse.dep13.eduforge.util.LecturerType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LecturerResTO implements Serializable {
    private Integer id;
    private String name;
    private String designation;
    private String qualification;
    private LecturerType type;
    private Integer displayOrder;
    private String picturePath;
    private String linkedin;
}
