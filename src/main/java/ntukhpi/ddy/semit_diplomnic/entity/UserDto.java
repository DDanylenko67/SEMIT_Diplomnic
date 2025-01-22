package ntukhpi.ddy.semit_diplomnic.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    @NotEmpty(message = "Ім'я не повинно бути пустим")
    private String name;
    @NotEmpty(message = "Пошта не повинна бути пустою")
    @Email
    private String email;
    @NotEmpty(message = "Пароль не повинен бути пустим")
    private String password;
    private String academicRang;
    private String academicDegree;
    private String universityGroup;
    private Supervisor supervisor;
    private StudentGroup studentGroup;
    private Theme theme;
    private Role role;

    public UserDto(){

    }

    public UserDto(String name, String email, String password, String academicRang, String academicDegree){
        this.name = name;
        this.email = email;
        this.password = password;
        this.academicRang = academicRang;
        this.academicDegree = academicDegree;
    }

    public UserDto(String name, String email, String universityGroup, Supervisor supervisor, StudentGroup studentGroup, String password){
        this.name = name;
        this.email = email;
        this.universityGroup = universityGroup;
        this.supervisor = supervisor;
        this.studentGroup = studentGroup;
        this.password = password;
    }


    public Long getId() {
        return id;
    }

    public @NotEmpty(message = "Ім'я не повинно бути пустим") String getName() {
        return name;
    }

    public @NotEmpty(message = "Пошта не повинна бути пустою") @Email String getEmail() {
        return email;
    }

    public @NotEmpty(message = "Пароль не повинен бути пустим") String getPassword() {
        return password;
    }

    public String getAcademicRang() {
        return academicRang;
    }

    public String getAcademicDegree() {
        return academicDegree;
    }

    public String getUniversityGroup() {
        return universityGroup;
    }

    public Supervisor getSupervisor() {
        return supervisor;
    }

    public StudentGroup getStudentGroup() {
        return studentGroup;
    }

    public Theme getTheme() {
        return theme;
    }

    public Role getRole() {
        return role;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(@NotEmpty(message = "Ім'я не повинно бути пустим") String name) {
        this.name = name;
    }

    public void setEmail(@NotEmpty(message = "Пошта не повинна бути пустою") @Email String email) {
        this.email = email;
    }

    public void setPassword(@NotEmpty(message = "Пароль не повинен бути пустим") String password) {
        this.password = password;
    }

    public void setAcademicRang(String academicRang) {
        this.academicRang = academicRang;
    }

    public void setAcademicDegree(String academicDegree) {
        this.academicDegree = academicDegree;
    }

    public void setUniversityGroup(String universityGroup) {
        this.universityGroup = universityGroup;
    }

    public void setSupervisor(Supervisor supervisor) {
        this.supervisor = supervisor;
    }

    public void setStudentGroup(StudentGroup studentGroup) {
        this.studentGroup = studentGroup;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}