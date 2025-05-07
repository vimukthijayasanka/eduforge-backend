package lk.ijse.dep13.eduforge.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "linkedin")
public class LinkedIn implements SuperEntity {
    @Id
    @Column(name = "lecturer_id")
    private Integer lecturerId;
    @MapsId
    @OneToOne
    @JoinColumn(name = "lecturer_id", referencedColumnName = "id")
    private Lecturer lecturer;
    @Column(nullable = false, length = 600)
    private String url;

    public LinkedIn(Lecturer lecturer, String url) {
        this.lecturer = lecturer;
        this.url = url;
    }
}
