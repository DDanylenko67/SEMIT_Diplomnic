package ntukhpi.ddy.semit_diplomnic.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
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

}