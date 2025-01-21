package ntukhpi.ddy.semit_diplomnic.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "supervisor")
public class Supervisor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100)
    String name;
    @Column(nullable = false, length = 30)
    String academicRang;
    @Column(nullable = false, length = 30)
    String academicDegree;
    @Column(nullable = false, length = 100)
    String email;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;


}
