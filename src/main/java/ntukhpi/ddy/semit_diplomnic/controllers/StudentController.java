package ntukhpi.ddy.semit_diplomnic.controllers;

import ntukhpi.ddy.semit_diplomnic.entity.Student;
import ntukhpi.ddy.semit_diplomnic.entity.Supervisor;
import ntukhpi.ddy.semit_diplomnic.entity.Theme;
import ntukhpi.ddy.semit_diplomnic.entity.User;
import ntukhpi.ddy.semit_diplomnic.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ntukhpi.ddy.semit_diplomnic.enums.status.status;
@Controller
public class StudentController {
        private final UserService userService;
        private final StudentGroupService studentGroupService;
        private final SupervisorService supervisorService;
        private final StudentService studentService;
        private final ThemeService themeService;
        private final TaskAssignmentService taskAssignmentService;
        private final TaskService taskService;
        public StudentController(UserService userService, StudentGroupService studentGroupService,
                               SupervisorService supervisorService, StudentService studentService,
                               ThemeService themeService, TaskAssignmentService taskAssignmentService,
                               TaskService taskService) {
            this.userService = userService;
            this.studentGroupService = studentGroupService;
            this.supervisorService = supervisorService;
            this.studentService = studentService;
            this.themeService = themeService;
            this.taskAssignmentService = taskAssignmentService;
            this.taskService = taskService;
        }
    @GetMapping("studentDetails/{id}")
    public String studentDetails(@PathVariable Long id, Model model, @RequestParam("group_id") Long groupId) {
        Student student = studentService.getStudentById(id);
        model.addAttribute("student", student);
        String themeUA = "";
        String themeENG = "";
        Theme theme = new Theme();
        if(student.getTheme() != null){
            if(student.getTheme().getThemeNameENG() != null){
                themeENG = student.getTheme().getThemeNameENG();
                theme.setThemeNameENG(themeENG);
            }
            themeUA = student.getTheme().getThemeNameUA();
            theme.setStatus(student.getTheme().getStatus());
            theme.setThemeNameUA(themeUA);
        }
        model.addAttribute("themeUA", themeUA);
        model.addAttribute("group_id", groupId);
        model.addAttribute("themeENG", themeENG);
        model.addAttribute("themeStatus", theme);
        return "/diplomnic/group/studentDetails";
    }
    @PostMapping("/diplomnic/updateStudent/{id}")
    public String updateStudent(@PathVariable Long id,  Model model, @RequestParam(value = "themeUA", required = false) String themeUA,
                                @RequestParam(value = "status", required = false) String status,
                                @RequestParam(value = "themeENG", required = false) String themeENG,
                                @ModelAttribute Student studentToSave){
        Student student = studentService.getStudentById(id);
        Supervisor supervisor = supervisorService.findSupervisorByEmail(getCurrentUser().getLogin());
        if(status != null && !themeUA.isEmpty()) {
            Theme theme = new Theme(themeUA, ntukhpi.ddy.semit_diplomnic.enums.status.status.suggested, supervisor, student);
            if(!themeENG.isEmpty()) {
                theme.setThemeNameENG(themeENG);
            }
            switch (status){
                case("suggested"):
                    theme.setStatus(ntukhpi.ddy.semit_diplomnic.enums.status.status.suggested);
                    break;
                case("done"):
                    theme.setStatus(ntukhpi.ddy.semit_diplomnic.enums.status.status.done);
                    break;
                case("checking"):
                    theme.setStatus(ntukhpi.ddy.semit_diplomnic.enums.status.status.checking);
                    break;
                case("rejecter"):
                    theme.setStatus(ntukhpi.ddy.semit_diplomnic.enums.status.status.rejected);
                    break;
            }
            themeService.saveTheme(theme);
        }
        else {
            if(!themeENG.equals(student.getTheme().getThemeNameENG())){
                Theme theme = themeService.getThemeByStudent(student);
                theme.setThemeNameENG(themeENG);
                themeService.updateTheme(theme.getId(), theme);
            }
            if(!themeUA.equals(student.getTheme().getThemeNameUA())){
                Theme theme = themeService.getThemeByStudent(student);
                theme.setThemeNameUA(themeUA);
                themeService.updateTheme(theme.getId(), theme);
            }
            if(!student.getTheme().getStatus().equals(studentToSave.getTheme().getStatus())){
                Theme theme = themeService.getThemeByStudent(student);
                theme.setStatus(studentToSave.getTheme().getStatus());
                themeService.updateTheme(theme.getId(), theme);
            }
        }
        return "redirect:/diplomnic";
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
