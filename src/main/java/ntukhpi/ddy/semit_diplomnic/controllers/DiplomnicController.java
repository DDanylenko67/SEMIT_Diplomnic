package ntukhpi.ddy.semit_diplomnic.controllers;

import ntukhpi.ddy.semit_diplomnic.entity.StudentGroup;
import ntukhpi.ddy.semit_diplomnic.entity.Supervisor;
import ntukhpi.ddy.semit_diplomnic.entity.User;
import ntukhpi.ddy.semit_diplomnic.repository.UserRepository;
import ntukhpi.ddy.semit_diplomnic.service.StudentGroupService;
import ntukhpi.ddy.semit_diplomnic.service.SupervisorService;
import ntukhpi.ddy.semit_diplomnic.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DiplomnicController {

    private final UserService userService;
    private final StudentGroupService studentGroupService;
    private final SupervisorService supervisorService;
    public DiplomnicController(UserService userService, StudentGroupService studentGroupService, SupervisorService supervisorService) {
        this.userService = userService;
        this.studentGroupService = studentGroupService;
        this.supervisorService = supervisorService;
    }
    @GetMapping("/diplomnic")
    public String diplomnic(Model model) {
        User user = getCurrentUser();
        if(user.getRoles().get(0).getName().equals("ROLE_SUPERVISOR")) {
            Supervisor supervisor = supervisorService.findSupervisorByEmail(user.getLogin());
            List<StudentGroup> groups = studentGroupService.getStudentsGroupBySupervisor(supervisor);
            model.addAttribute("groups", groups);
        }
        return "/diplomnic/diplomnic";
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated()) {
            String username = authentication.getName();
            return userService.findUserByEmail(username);
        }
        return null;
    }


}
