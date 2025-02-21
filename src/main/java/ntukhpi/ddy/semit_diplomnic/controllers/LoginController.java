package ntukhpi.ddy.semit_diplomnic.controllers;

import jakarta.validation.Valid;
import ntukhpi.ddy.semit_diplomnic.entity.User;
import ntukhpi.ddy.semit_diplomnic.entity.UserDto;
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
}
