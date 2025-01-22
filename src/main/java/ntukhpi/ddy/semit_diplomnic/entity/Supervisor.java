package ntukhpi.ddy.semit_diplomnic.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Entity
@Table(name = "supervisor")
public class Supervisor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100)
    String name;
    @Column(length = 30)
    String academicRang;
    @Column(length = 30)
    String academicDegree;
    @Column(nullable = false, length = 100)
    String email;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAcademicRang() {
        return academicRang;
    }

    public String getAcademicDegree() {
        return academicDegree;
    }

    public String getEmail() {
        return email;
    }

    public User getUser() {
        return user;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAcademicRang(String academicRang) {
        this.academicRang = academicRang;
    }

    public void setAcademicDegree(String academicDegree) {
        this.academicDegree = academicDegree;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Supervisor(){

    }

    public Supervisor (String name, String academicRang, String academicDegree, String email) {
        this.name = name;
        this.academicRang = academicRang;
        this.academicDegree = academicDegree;
        this.email = email;
    }
}
