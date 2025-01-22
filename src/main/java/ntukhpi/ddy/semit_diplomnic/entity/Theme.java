package ntukhpi.ddy.semit_diplomnic.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ntukhpi.ddy.semit_diplomnic.enums.status.status;
import ntukhpi.ddy.semit_diplomnic.enums.status.statusConverter;


@NoArgsConstructor
@Entity
@Table(name = "theme")
public class Theme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 250)
    private String themeNameUA;
    @Column(nullable = false, length = 250)
    private String themeNameENG;
    @Column
    private String comment;
    @Enumerated(EnumType.STRING)
    @Column(name = "themeStatus")
    @Convert(converter = statusConverter.class)
    private status status;
    @ManyToOne
    @JoinColumn(name = "supervisor_id")
    private Supervisor supervisor;
    @OneToOne
    @JoinColumn(name = "student_id")
    private Student student;

    public Theme(){

    }

    public Theme(String themeNameUA, String themeNameENG,  ntukhpi.ddy.semit_diplomnic.enums.status.status status, Supervisor supervisor, Student student) {
        this.themeNameUA = themeNameUA;
        this.themeNameENG = themeNameENG;
        this.status = status;
        this.supervisor = supervisor;
        this.student = student;
    }

    public Long getId() {
        return id;
    }

    public String getThemeNameUA() {
        return themeNameUA;
    }

    public String getThemeNameENG() {
        return themeNameENG;
    }

    public String getComment() {
        return comment;
    }

    public ntukhpi.ddy.semit_diplomnic.enums.status.status getStatus() {
        return status;
    }

    public Supervisor getSupervisor() {
        return supervisor;
    }

    public Student getStudent() {
        return student;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setThemeNameUA(String themeNameUA) {
        this.themeNameUA = themeNameUA;
    }

    public void setThemeNameENG(String themeNameENG) {
        this.themeNameENG = themeNameENG;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setStatus(ntukhpi.ddy.semit_diplomnic.enums.status.status status) {
        this.status = status;
    }

    public void setSupervisor(Supervisor supervisor) {
        this.supervisor = supervisor;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
