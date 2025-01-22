package ntukhpi.ddy.semit_diplomnic.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


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
    @ManyToOne
    @JoinColumn(name = "supervisor_id", unique = false)
    private Supervisor supervisor;
    @ManyToOne
    @JoinColumn(name = "studentGroup_id", unique = false)
    private StudentGroup group;
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

    public Supervisor getSupervisor() {
        return supervisor;
    }

    public StudentGroup getGroup() {
        return group;
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

    public void setSupervisor(Supervisor supervisor) {
        this.supervisor = supervisor;
    }

    public void setGroup(StudentGroup group) {
        this.group = group;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
