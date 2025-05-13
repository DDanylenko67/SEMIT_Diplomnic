package ntukhpi.ddy.semit_diplomnic.entity.tools;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import ntukhpi.ddy.semit_diplomnic.entity.Role;
import ntukhpi.ddy.semit_diplomnic.entity.StudentGroup;
import ntukhpi.ddy.semit_diplomnic.entity.Supervisor;
import ntukhpi.ddy.semit_diplomnic.entity.Theme;

import java.util.List;

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
    private List<StudentGroup> studentGroups;
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

    public UserDto(String name, String email, String universityGroup,  List<StudentGroup> studentGroups, String password){
        this.name = name;
        this.email = email;
        this.universityGroup = universityGroup;
        this.studentGroups = studentGroups;
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

    public List<StudentGroup> getStudentGroups() {
        return studentGroups;
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

    public void setStudentGroup(List<StudentGroup> studentGroup) {
        this.studentGroups = studentGroup;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}