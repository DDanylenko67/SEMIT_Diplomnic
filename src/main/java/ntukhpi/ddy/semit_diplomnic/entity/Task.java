package ntukhpi.ddy.semit_diplomnic.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ntukhpi.ddy.semit_diplomnic.enums.status.status;
import ntukhpi.ddy.semit_diplomnic.enums.status.statusConverter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


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
    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate deadline;
    @Column
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfUpdate;
    @ManyToOne
    @JoinColumn(name = "supervisor_id")
    private Supervisor supervisor;
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<TaskAssignment> assignments = new ArrayList<>();
    @ManyToMany(mappedBy = "tasks", fetch = FetchType.EAGER)
    private List<StudentGroup> studentGroups = new ArrayList<>();
    public Task(){

    }


    public Task(String title, String description, Supervisor supervisor, LocalDate deadLine, List<StudentGroup> groups) {
        this.title = title;
        this.dateOfCreate = LocalDate.now();
        this.description = description;
        this.supervisor = supervisor;
        this.deadline = deadLine;
        this.studentGroups = groups;
    }
    public void setStudentGroups(List<StudentGroup> studentGroups) {
        this.studentGroups = studentGroups;
    }

    public List<StudentGroup> getStudentGroups() {
        return studentGroups;
    }

    public List<TaskAssignment> getAssignments() {
        return assignments;
    }

    public LocalDate getDeadline() {
        return deadline;
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

    public Supervisor getSupervisor() {
        return supervisor;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public void setAssignments(List<TaskAssignment> assignments) {
        this.assignments = assignments;
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


    public void setSupervisor(Supervisor supervisor) {
        this.supervisor = supervisor;
    }

}
