package ntukhpi.ddy.semit_diplomnic.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ntukhpi.ddy.semit_diplomnic.enums.groupType.groupType;
import ntukhpi.ddy.semit_diplomnic.enums.groupType.groupTypeConverter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "studentGroup")
public class StudentGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 30)
    String groupName;
    @Enumerated(EnumType.STRING)
    @Column(name = "groupType", nullable = false)
    @Convert(converter = groupTypeConverter.class)
    private groupType groupType;
    @OneToOne
    @JoinColumn(name = "supervisor_id")
    private Supervisor supervisor;
    @OneToMany(mappedBy = "group")
    private List<Student> students = new ArrayList<>();
}
