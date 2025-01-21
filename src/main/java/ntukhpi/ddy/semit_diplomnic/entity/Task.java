package ntukhpi.ddy.semit_diplomnic.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ntukhpi.ddy.semit_diplomnic.enums.status.status;
import ntukhpi.ddy.semit_diplomnic.enums.status.statusConverter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
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
    private LocalDate dateOfUpdate;
    @Enumerated(EnumType.STRING)
    @Column(name = "themeStatus", nullable = false)
    @Convert(converter = statusConverter.class)
    private status status;
    @OneToOne
    @JoinColumn(name = "supervisor_id")
    private Supervisor supervisor;
    @OneToOne
    @JoinColumn(name = "student_id")
    private Student student;
}
