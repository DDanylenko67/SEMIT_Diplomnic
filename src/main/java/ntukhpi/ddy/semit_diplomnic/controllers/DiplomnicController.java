package ntukhpi.ddy.semit_diplomnic.controllers;

import ntukhpi.ddy.semit_diplomnic.entity.*;
import ntukhpi.ddy.semit_diplomnic.enums.status.status;
import ntukhpi.ddy.semit_diplomnic.repository.UserRepository;
import ntukhpi.ddy.semit_diplomnic.service.StudentGroupService;
import ntukhpi.ddy.semit_diplomnic.service.SupervisorService;
import ntukhpi.ddy.semit_diplomnic.service.TaskAssignmentService;
import ntukhpi.ddy.semit_diplomnic.service.UserService;
import org.hibernate.sql.ast.tree.update.Assignment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class DiplomnicController {

    private final UserService userService;
    private final StudentGroupService studentGroupService;
    private final SupervisorService supervisorService;
    private final TaskAssignmentService taskAssignmentService;
    public DiplomnicController(UserService userService, StudentGroupService studentGroupService, SupervisorService supervisorService, TaskAssignmentService taskAssignmentService) {
        this.userService = userService;
        this.studentGroupService = studentGroupService;
        this.supervisorService = supervisorService;
        this.taskAssignmentService = taskAssignmentService;
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
    @GetMapping("/checkTask")
    public String checkTask(Model model) {
        Supervisor supervisor = supervisorService.findSupervisorByEmail(getCurrentUser().getLogin());
        List<StudentGroup> groups = studentGroupService.getStudentsGroupBySupervisor(supervisor);
        List<TaskAssignment> taskAssignments = new ArrayList<>();
        for(TaskAssignment assignment : taskAssignmentService.getAllTaskAssignments()) {
            if(assignment.getStatus().equals(status.checking)){
                taskAssignments.add(assignment);
            }
        }
        model.addAttribute("tasks", taskAssignments);
        return "/diplomnic/checkTask";
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
