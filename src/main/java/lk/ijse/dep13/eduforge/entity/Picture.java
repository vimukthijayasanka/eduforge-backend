package lk.ijse.dep13.eduforge.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "picture")
public class Picture implements SuperEntity {
    @Id
    @Column(name = "lecturer_id")
    private Integer lecturerId;
    @MapsId
    @OneToOne
    @JoinColumn(name = "lecturer_id", referencedColumnName = "id")
    private Lecturer lecturer;
    @Column(name="picture_path", nullable = false, length = 400)
    private String picturePath;

    public Picture(Lecturer lecturer, String picturePath) {
        this.lecturer = lecturer;
        this.picturePath = picturePath;
    }
}
