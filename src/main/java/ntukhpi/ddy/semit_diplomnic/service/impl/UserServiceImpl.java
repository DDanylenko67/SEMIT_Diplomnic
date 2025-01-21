package ntukhpi.ddy.semit_diplomnic.service.impl;

import ntukhpi.ddy.semit_diplomnic.entity.*;
import ntukhpi.ddy.semit_diplomnic.repository.RoleRepository;
import ntukhpi.ddy.semit_diplomnic.repository.StudentRepository;
import ntukhpi.ddy.semit_diplomnic.repository.SupervisorRepository;
import ntukhpi.ddy.semit_diplomnic.repository.UserRepository;
import ntukhpi.ddy.semit_diplomnic.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private SupervisorRepository supervisorRepository;
    private StudentRepository studentRepository;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           SupervisorRepository supervisorRepository,
                           StudentRepository studentRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.studentRepository = studentRepository;
        this.supervisorRepository = supervisorRepository;
    }

    @Override
    public void saveUserSupervisor(UserDto userDto) {
        createUser(userDto, "ROLE_SUPERVISOR");
        Supervisor supervisor = new Supervisor();
        supervisor.setName(userDto.getName());
        supervisor.setEmail(userDto.getEmail());
        supervisor.setAcademicRang(userDto.getAcademicRang());
        supervisor.setAcademicDegree(userDto.getAcademicDegree());
        supervisor.setUser(userRepository.findByLogin(userDto.getEmail()));
        supervisorRepository.save(supervisor);
    }

    @Override
    public void saveUserStudent(UserDto userDto) {
        createUser(userDto, "ROLE_STUDENT");
        Student student = new Student();
        student.setName(userDto.getName());
        student.setEmail(userDto.getEmail());
        student.setUniversityGroup(userDto.getUniversityGroup());
        student.setSupervisor(userDto.getSupervisor());
        student.setGroup(userDto.getStudentGroup());
        student.setUser(userRepository.findByLogin(userDto.getEmail()));
        studentRepository.save(student);
    }

    private void createUser(UserDto userDto, String roles) {
        User user = new User();
        user.setLogin(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.now();
        String formattedDate = date.format(formatter);
        user.setDateOfCreate(LocalDate.now());
        Role role = roleRepository.findByName(roles);
        if(role == null){
            role = checkRoleExist();
        }
        user.setRoles(Arrays.asList(role));
        userRepository.save(user);
    }
    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByLogin(email);
    }


    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map((user) -> mapToUserDto(user))
                .collect(Collectors.toList());
    }

    private UserDto mapToUserDto(User user){
        UserDto userDto = new UserDto();
        if(user.getRoles().get(0).getName().equals("ROLE_STUDENT)")){
            Student student = studentRepository.findByEmail(user.getLogin());
            userDto.setName(student.getName());
            userDto.setEmail(student.getEmail());
            userDto.setUniversityGroup(student.getUniversityGroup());
            userDto.setSupervisor(student.getSupervisor());
            userDto.setStudentGroup(student.getGroup());
            userDto.setTheme(student.getTheme());
            userDto.setRole(user.getRoles().get(0));
        }
        else{
            Supervisor supervisor = supervisorRepository.findByEmail(user.getLogin());
            userDto.setName(supervisor.getName());
            userDto.setAcademicRang(supervisor.getAcademicRang());
            userDto.setAcademicDegree(supervisor.getAcademicDegree());
            userDto.setEmail(supervisor.getEmail());
            userDto.setRole(user.getRoles().get(0));
        }
        return userDto;
    }

    private Role checkRoleExist(){
        Role role = new Role();
        role.setName("ROLE_SUPERVISOR");
        return roleRepository.save(role);
    }

}
