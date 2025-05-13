package ntukhpi.ddy.semit_diplomnic.controllers;

import jakarta.validation.Valid;
import ntukhpi.ddy.semit_diplomnic.entity.User;
import ntukhpi.ddy.semit_diplomnic.entity.tools.UserDto;
import ntukhpi.ddy.semit_diplomnic.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    private UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/login")
    public String login(Model model) {
        return "/login/login";
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/diplomnic";
    }

    @GetMapping("/?continue")
    public String homes() {
        return "redirect:/diplomnic";
    }

    @GetMapping("/diplomnic/signUp")
    public String signUp(Model model) {
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "/login/signup";
    }
    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto userDto,
                               BindingResult result,
                               Model model){
        User existingUser = userService.findUserByEmail(userDto.getEmail());

        if(existingUser != null && existingUser.getLogin() != null && !existingUser.getLogin().isEmpty()){
            return "redirect:/login?exist";
        }

        if(result.hasErrors()){
            model.addAttribute("user", userDto);
            return "/login/signup";
        }
        if(userDto.getAcademicRang().isEmpty()){
            userDto.setAcademicRang("");
        }
        UserDto supervisorToSave = new UserDto(userDto.getName(), userDto.getEmail(), userDto.getPassword(),
                userDto.getAcademicRang(), userDto.getAcademicDegree());
        userService.saveUserSupervisor(supervisorToSave);
        return "redirect:/login?success";
    }
}
