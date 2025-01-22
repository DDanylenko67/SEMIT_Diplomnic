package ntukhpi.ddy.semit_diplomnic.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ntukhpi.ddy.semit_diplomnic.enums.groupType.groupType;
import ntukhpi.ddy.semit_diplomnic.enums.groupType.groupTypeConverter;

import java.util.ArrayList;
import java.util.List;

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
    @ManyToOne
    @JoinColumn(name = "supervisor_id")
    private Supervisor supervisor;
    @OneToMany(mappedBy = "group", fetch = FetchType.EAGER)
    private List<Student> students = new ArrayList<>();
    public StudentGroup(){

    }
    public StudentGroup(String groupName, groupType groupType, Supervisor supervisor) {
        this.groupName = groupName;
        this.groupType = groupType;
        this.supervisor = supervisor;
    }
    public Long getId() {
        return id;
    }

    public String getGroupName() {
        return groupName;
    }

    public ntukhpi.ddy.semit_diplomnic.enums.groupType.groupType getGroupType() {
        return groupType;
    }

    public Supervisor getSupervisor() {
        return supervisor;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setGroupType(ntukhpi.ddy.semit_diplomnic.enums.groupType.groupType groupType) {
        this.groupType = groupType;
    }

    public void setSupervisor(Supervisor supervisor) {
        this.supervisor = supervisor;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}
