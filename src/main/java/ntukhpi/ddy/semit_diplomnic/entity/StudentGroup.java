package ntukhpi.ddy.semit_diplomnic.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ntukhpi.ddy.semit_diplomnic.enums.groupType.groupType;
import ntukhpi.ddy.semit_diplomnic.enums.groupType.groupTypeConverter;
import ntukhpi.ddy.semit_diplomnic.enums.status.status;


import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Entity
@Table(name = "student_group")
public class StudentGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 10)
    String code;
    @Column(nullable = false, length = 50)
    String groupName;
    @Enumerated(EnumType.STRING)
    @Column(name = "groupType", nullable = false)
    @Convert(converter = groupTypeConverter.class)
    private groupType groupType;
    @ManyToOne
    @JoinColumn(name = "supervisor_id")
    private Supervisor supervisor;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "students_in_groups",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private List<Student> students = new ArrayList<>();
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "task_student_group",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "task_id")
    )
    private List<Task> tasks = new ArrayList<>();

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public StudentGroup(){

    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public StudentGroup(String groupName, groupType groupType, Supervisor supervisor, String code) {
        this.groupName = groupName;
        this.groupType = groupType;
        this.supervisor = supervisor;
        this.code = code;
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

    public int getStatusByStudent(Long id) {
        int size = 0;
        for(Task task : tasks){
            for(TaskAssignment assignment : task.getAssignments()){
                if(assignment.getStudent().getId() == id && assignment.getStudentGroup().getId().equals(this.id) && assignment.getStatus().equals(status.done)){
                    size++;
                }
            }
        }
        return size;
    }
}
