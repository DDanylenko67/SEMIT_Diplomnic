package ntukhpi.ddy.semit_diplomnic.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import ntukhpi.ddy.semit_diplomnic.enums.status.status;
import ntukhpi.ddy.semit_diplomnic.enums.status.statusConverter;

@NoArgsConstructor
@Entity
@Table(name = "task_assignments")
public class TaskAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "studentGroup_id", nullable = false)
    private StudentGroup studentGroup;

    @Enumerated(EnumType.STRING)
    @Column(name = "taskStatus")
    @Convert(converter = statusConverter.class)
    private status Status;
    public TaskAssignment() {
    }
    public TaskAssignment(Task task, Student student, StudentGroup studentGroup,status Status) {
        this.task = task;
        this.student = student;
        this.studentGroup = studentGroup;;
        this.Status = Status;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public StudentGroup getStudentGroup() {
        return studentGroup;
    }

    public void setStudentGroup(StudentGroup studentGroup) {
        this.studentGroup = studentGroup;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setStatus(status status) {
        Status = status;
    }

    public status getStatus() {
        return Status;
    }

    public Student getStudent() {
        return student;
    }

    public Task getTask() {
        return task;
    }

    public Long getId() {
        return id;
    }
}
