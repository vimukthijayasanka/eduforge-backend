package lk.ijse.dep13.eduforge.entity;

import jakarta.persistence.*;
import lk.ijse.dep13.eduforge.util.LecturerType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "lecturer")
public class Lecturer implements Serializable {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false, length = 300)
    private String name;
    @Column(nullable = false, length = 600)
    private String designation;
    @Column(nullable = false, length = 600)
    private String qualification;
    @Column(nullable = false, columnDefinition = "ENUM('FULL_TIME','VISITING')")
    @Enumerated(EnumType.STRING)
    private LecturerType type;
    @Column(nullable = false, name = "display_order")
    private int displayOrder;

    @ToString.Exclude
    @OneToOne(mappedBy = "lecturer")
    private Picture picture;

    @ToString.Exclude
    @OneToOne(mappedBy = "lecturer")
    private LinkedIn linkedin;

    public Lecturer(String name, String designation, String qualification, LecturerType type, int displayOrder) {
        this.name = name;
        this.designation = designation;
        this.qualification = qualification;
        this.type = type;
        this.displayOrder = displayOrder;
    }

    public Lecturer(Integer id, String name, String designation, String qualification, LecturerType type, int displayOrder) {
        this.id = id;
        this.name = name;
        this.designation = designation;
        this.qualification = qualification;
        this.type = type;
        this.displayOrder = displayOrder;
    }

    public void setPicture(Picture picture) {
        if (picture != null) picture.setLecturer(this);
        this.picture = picture;
    }

    public void setLinkedin(LinkedIn linkedin) {
        if (linkedin != null) linkedin.setLecturer(this);
        this.linkedin = linkedin;
    }
}
