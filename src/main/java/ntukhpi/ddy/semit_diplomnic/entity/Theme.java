package ntukhpi.ddy.semit_diplomnic.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ntukhpi.ddy.semit_diplomnic.enums.status.status;
import ntukhpi.ddy.semit_diplomnic.enums.status.statusConverter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "theme")
public class Theme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100)
    private String themeNameUA;
    @Column(nullable = false, length = 100)
    private String themeNameENG;
    @Column
    private String comment;
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
