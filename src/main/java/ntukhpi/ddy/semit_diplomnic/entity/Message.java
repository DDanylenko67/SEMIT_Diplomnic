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

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    @ManyToOne
    @JoinColumn(name = "supervisor_id")
    private Supervisor supervisorSender;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student studentSender;

    @ManyToOne
    @JoinColumn(name = "supervisor_receiver_id")
    private Supervisor supervisorReceiver;

    @ManyToOne
    @JoinColumn(name = "student_receiver_id")
    private Student studentReceiver;

    public Message(){

    }

    public Message(String text, LocalDateTime timestamp, Task task, Supervisor supervisorSender, Student studentReceiver) {
        this.text = text;
        this.timestamp = timestamp;
        this.task = task;
        this.supervisorSender = supervisorSender;
        this.studentReceiver = studentReceiver;
    }

    public Message(String text, LocalDateTime timestamp, Task task, Student studentSender, Supervisor supervisorReceiver) {
        this.text = text;
        this.timestamp = timestamp;
        this.task = task;
        this.studentSender = studentSender;
        this.supervisorReceiver = supervisorReceiver;
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

    public Task getTask() {
        return task;
    }

    public Supervisor getSupervisorSender() {
        return supervisorSender;
    }

    public Student getStudentSender() {
        return studentSender;
    }

    public Supervisor getSupervisorReceiver() {
        return supervisorReceiver;
    }

    public Student getStudentReceiver() {
        return studentReceiver;
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

    public void setTask(Task task) {
        this.task = task;
    }

    public void setSupervisorSender(Supervisor supervisorSender) {
        this.supervisorSender = supervisorSender;
    }

    public void setStudentSender(Student studentSender) {
        this.studentSender = studentSender;
    }

    public void setSupervisorReceiver(Supervisor supervisorReceiver) {
        this.supervisorReceiver = supervisorReceiver;
    }

    public void setStudentReceiver(Student studentReceiver) {
        this.studentReceiver = studentReceiver;
    }
}
