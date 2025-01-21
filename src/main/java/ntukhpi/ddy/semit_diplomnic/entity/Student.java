package ntukhpi.ddy.semit_diplomnic.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "student")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false, length = 100)
    private String name;
    @Column(nullable = false, length = 100)
    private String email;
    @Column(nullable = false, length = 10)
    private String universityGroup;
    @OneToOne
    @JoinColumn(name = "supervisor_id")
    private Supervisor supervisor;
    @ManyToOne
    @JoinColumn(name = "studentGroup_id")
    private StudentGroup group;
    @OneToOne
    @JoinColumn(name = "theme_id")
    private Theme theme;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
