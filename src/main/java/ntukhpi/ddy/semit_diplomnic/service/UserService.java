package ntukhpi.ddy.semit_diplomnic.service;

import ntukhpi.ddy.semit_diplomnic.entity.User;
import ntukhpi.ddy.semit_diplomnic.entity.UserDto;

import java.util.List;

public interface UserService {
    void saveUserSupervisor(UserDto userDto);
    void saveUserStudent(UserDto userDto);
    User findUserByEmail(String email);
    List<UserDto> findAllUsers();
}
