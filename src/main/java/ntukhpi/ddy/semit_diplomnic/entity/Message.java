package ntukhpi.ddy.semit_diplomnic.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;


@NoArgsConstructor
@Entity
@Table(name = "message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String text;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private boolean isStudentSender;

    @ManyToOne
    @JoinColumn(name = "supervisor_id")
    private Supervisor supervisor;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "assignment_id")
    private TaskAssignment assignment;

    public Message(){

    }

    public Message(String text, LocalDateTime timestamp, boolean isStudentSender, Supervisor supervisor, Student student, TaskAssignment assignment) {
        this.text = text;
        this.timestamp = timestamp;
        this.isStudentSender = isStudentSender;
        this.student = student;
        this.supervisor = supervisor;
        this.assignment = assignment;
    }


    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public boolean isStudentSender() {
        return isStudentSender;
    }

    public Supervisor getSupervisor() {
        return supervisor;
    }

    public Student getStudent() {
        return student;
    }

    public TaskAssignment getAssignment() {
        return assignment;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }


    public void setSupervisor(Supervisor supervisor) {
        this.supervisor = supervisor;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setAssignment(TaskAssignment assignment) {
        this.assignment = assignment;
    }

    public void setStudentSender(boolean studentSender) {
        isStudentSender = studentSender;
    }
}
