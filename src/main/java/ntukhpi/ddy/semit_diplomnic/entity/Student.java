package ntukhpi.ddy.semit_diplomnic.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
@Entity
@Table(name = "student")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100)
    private String name;
    @Column(nullable = false, length = 100)
    private String email;
    @Column(nullable = false, length = 10)
    private String universityGroup;
    @ManyToMany(mappedBy = "students")
    private List<StudentGroup> groups = new ArrayList<>();
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<TaskAssignment> assignments = new ArrayList<>();

    public Student() {
    }

    public Student(String name, String email, String universityGroup, List<StudentGroup> groups) {
        this.name = name;
        this.email = email;
        this.universityGroup = universityGroup;
        this.groups = groups;
    }

    public void setGroups(List<StudentGroup> groups) {
        this.groups = groups;
    }

    public void setAssignments(List<TaskAssignment> assignments) {
        this.assignments = assignments;
    }

    public List<TaskAssignment> getAssignments() {
        return assignments;
    }

    public List<StudentGroup> getGroups() {
        return groups;
    }

    @OneToOne
    @JoinColumn(name = "theme_id", unique = false)
    private Theme theme;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getUniversityGroup() {
        return universityGroup;
    }


    public List<StudentGroup> getGroup() {
        return groups;
    }

    public Theme getTheme() {
        return theme;
    }

    public User getUser() {
        return user;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUniversityGroup(String universityGroup) {
        this.universityGroup = universityGroup;
    }

    public void setGroup(List<StudentGroup> group) {
        this.groups = group;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
