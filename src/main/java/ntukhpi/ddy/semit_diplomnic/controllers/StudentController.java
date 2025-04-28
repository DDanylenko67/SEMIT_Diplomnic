package ntukhpi.ddy.semit_diplomnic.controllers;

import ntukhpi.ddy.semit_diplomnic.entity.*;
import ntukhpi.ddy.semit_diplomnic.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ntukhpi.ddy.semit_diplomnic.enums.status.status;

import java.util.ArrayList;
import java.util.List;

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
        if(status != null && themeUA.isEmpty()){
            return "redirect:/diplomnic";
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

    @GetMapping("/selectTheme")
    public String selectTheme(Model model){
            Student student = studentService.getStudentByEmail(getCurrentUser().getLogin());
            Theme theme = student.getTheme();
            if(theme != null){
                model.addAttribute("theme", theme);
            }
            else{
                return "redirect:/suggestTheme";
            }
            model.addAttribute("student", student);
            return "/diplomnic/theme/selectTheme";
    }
    @PostMapping("/diplomnic/submitTheme")
    public String submitTheme(Model model){
            Student student = studentService.getStudentByEmail(getCurrentUser().getLogin());
            Theme theme = student.getTheme();
            theme.setStatus(status.done);
            themeService.updateTheme(theme.getId(), theme);
            return "redirect:/diplomnic";
    }
    @GetMapping("/suggestTheme")
    public String suggestTheme(Model model){
        Student student = studentService.getStudentByEmail(getCurrentUser().getLogin());
        List<StudentGroup> studentGroups = student.getGroups();
        List<Supervisor> supervisors = new ArrayList<>();
        if(!studentGroups.isEmpty()){
            for(StudentGroup studentGroup : studentGroups){
                if(!supervisors.contains(studentGroup.getSupervisor())){
                    supervisors.add(studentGroup.getSupervisor());
                }
            }
        }
        model.addAttribute("supervisors", supervisors);
        model.addAttribute("student", student);
        String themeUA = "";
        String themeENG = "";
        model.addAttribute("themeUA", themeUA);
        model.addAttribute("themeENG", themeENG);
        return "/diplomnic/theme/suggestTheme";
    }
    @PostMapping("/diplomnic/saveSuggestTheme")
    public String saveSuggestTheme(Model model,
                                   @RequestParam String themeUA,
                                   @RequestParam(required = false) String themeENG,
                                   @RequestParam(value = "status", required = false) Long id,
                                   @RequestParam(value = "supervisor_id", required = false) Long supervisor_id) {
        if (supervisor_id == null && id != null) {
            supervisor_id = id;
        }
        Supervisor supervisor = supervisorService.getSupervisorById(supervisor_id);
        Student student = studentService.getStudentByEmail(getCurrentUser().getLogin());
        Theme theme;
        if(themeENG == null || themeENG.isEmpty() || themeENG.equals(" ")){
            theme = new Theme(themeUA, status.suggested, supervisor, student);
        }
        else {
            theme = new Theme(themeUA, themeENG,status.suggested, supervisor, student);
        }
        if(student.getTheme() != null){
            Theme themeOfStudent = student.getTheme();
            themeService.updateTheme(themeOfStudent.getId(), theme);
        }
        else {
            themeService.saveTheme(theme);
        }
        student.setTheme(theme);
        studentService.updateStudent(student.getId(), student);
        return "redirect:/diplomnic";
    }
    @GetMapping("/admitTheme/student/{id}")
    public String admitTheme(Model model, @PathVariable Long id){
        Student student = studentService.getStudentById(id);
        Theme theme = student.getTheme();
        model.addAttribute("student", student);
        model.addAttribute("theme", theme);
        return "/diplomnic/theme/admitTheme";
    }
    @PostMapping("/diplomnic/admitTheme{id}")
    public String admitOfStudentTheme(Model model,@PathVariable Long id){
            Theme theme = themeService.getThemeById(id);
            theme.setStatus(status.done);
            themeService.updateTheme(theme.getId(), theme);
            return "redirect:/diplomnic";
    }
    @PostMapping("/diplomnic/rejectTheme{id}" )
    public String rejectTheme(Model model, @PathVariable Long id){
        Theme theme = themeService.getThemeById(id);
        theme.setStatus(status.rejected);
        themeService.updateTheme(theme.getId(), theme);
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
