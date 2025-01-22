package ntukhpi.ddy.semit_diplomnic.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ntukhpi.ddy.semit_diplomnic.enums.status.status;
import ntukhpi.ddy.semit_diplomnic.enums.status.statusConverter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;


@NoArgsConstructor
@Entity
@Table(name = "task")
public class Task {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 50)
    private String title;
    @Column(nullable = false, length = 150)
    private String description;
    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfCreate;
    @Column
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfUpdate;
    @Enumerated(EnumType.STRING)
    @Column(name = "themeStatus")
    @Convert(converter = statusConverter.class)
    private status status;
    @ManyToOne
    @JoinColumn(name = "supervisor_id")
    private Supervisor supervisor;
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    public Task(){

    }

    public Task(String title, String description, Supervisor supervisor, Student student) {
        this.title = title;
        this.dateOfCreate = LocalDate.now();
        this.description = description;
        this.supervisor = supervisor;
        this.student = student;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDateOfCreate() {
        return dateOfCreate;
    }

    public LocalDate getDateOfUpdate() {
        return dateOfUpdate;
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

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDateOfCreate(LocalDate dateOfCreate) {
        this.dateOfCreate = dateOfCreate;
    }

    public void setDateOfUpdate(LocalDate dateOfUpdate) {
        this.dateOfUpdate = dateOfUpdate;
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
