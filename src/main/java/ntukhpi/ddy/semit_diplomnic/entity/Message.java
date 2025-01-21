package ntukhpi.ddy.semit_diplomnic.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
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

    @OneToOne
    @JoinColumn(name = "task_id")
    private Task task;

    @OneToOne
    @JoinColumn(name = "supervisor_id", nullable = true)
    private Supervisor supervisorSender;

    @OneToOne
    @JoinColumn(name = "student_id", nullable = true)
    private Student studentSender;

    @OneToOne
    @JoinColumn(name = "supervisor_receiver_id", nullable = true)
    private Supervisor supervisorReceiver;

    @OneToOne
    @JoinColumn(name = "student_receiver_id", nullable = true)
    private Student studentReceiver;
}
