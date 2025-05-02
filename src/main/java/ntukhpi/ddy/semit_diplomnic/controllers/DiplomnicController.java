package ntukhpi.ddy.semit_diplomnic.controllers;

import jakarta.servlet.http.HttpServletRequest;
import ntukhpi.ddy.semit_diplomnic.entity.*;
import ntukhpi.ddy.semit_diplomnic.enums.status.status;
import ntukhpi.ddy.semit_diplomnic.repository.UserRepository;
import ntukhpi.ddy.semit_diplomnic.service.*;
import org.hibernate.sql.ast.tree.update.Assignment;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

@Controller
public class DiplomnicController {

    private final UserService userService;
    private final StudentGroupService studentGroupService;
    private final SupervisorService supervisorService;
    private final TaskAssignmentService taskAssignmentService;
    private final StudentService studentService;
    private final ThemeService themeService;
    public DiplomnicController(UserService userService, StudentGroupService studentGroupService, StudentService studentService,
                               SupervisorService supervisorService, TaskAssignmentService taskAssignmentService, ThemeService themeService) {
        this.userService = userService;
        this.studentGroupService = studentGroupService;
        this.supervisorService = supervisorService;
        this.taskAssignmentService = taskAssignmentService;
        this.studentService = studentService;
        this.themeService = themeService;
    }
    @GetMapping("/diplomnic")
    public String diplomnic(Model model) {
        User user = getCurrentUser();
        if(user.getRoles().get(0).getName().equals("ROLE_SUPERVISOR")) {
            Supervisor supervisor = supervisorService.findSupervisorByEmail(user.getLogin());
            List<StudentGroup> groups = studentGroupService.getStudentsGroupBySupervisor(supervisor);
            model.addAttribute("groups", groups);
        }
        else {
            Student student = studentService.getStudentByEmail(user.getLogin());
            List<TaskAssignment> task = student.getAssignments();
            String type = "all";
            model.addAttribute("type", type);
            model.addAttribute("task", task);
        }
        return "/diplomnic/diplomnic";
    }

    @GetMapping("/information")
    public String information(Model model){
        addAuthToModel(model);
        return "/diplomnic/information";
    }
    @GetMapping("/diplomnic/editInformation")
    public String editInformation(Model model){
        addAuthToModel(model);
        return "/diplomnic/editInformation";
    }

    @PostMapping("/diplomnic/saveEditedInformation")
    public String saveEditedSupervisor(HttpServletRequest request, @ModelAttribute("supervisor") Supervisor supervisorForm,
                                       @ModelAttribute("student") Student studentForm){
        String formType = request.getParameter("formType");
        if ("supervisor".equals(formType)) {
            Supervisor currentSupervisor = supervisorService.findSupervisorByEmail(getCurrentUser().getLogin());
            currentSupervisor.setName(supervisorForm.getName());
            if (!supervisorForm.getAcademicDegree().isEmpty() && supervisorForm.getAcademicDegree() != null) {
                currentSupervisor.setAcademicDegree(supervisorForm.getAcademicDegree());
            }
            if(!supervisorForm.getAcademicRang().isEmpty() && supervisorForm.getAcademicRang() != null) {
                currentSupervisor.setAcademicRang(supervisorForm.getAcademicRang());
            }
            User user = userService.findUserByEmail(currentSupervisor.getEmail());
            user.setLogin(supervisorForm.getEmail());
            userService.updateUser(user.getId(), user);
            currentSupervisor.setEmail(supervisorForm.getEmail());
            supervisorService.updateSupervisor(currentSupervisor.getId(), currentSupervisor);
        }else {
            Student currentStudent = studentService.getStudentByEmail(getCurrentUser().getLogin());
            currentStudent.setName(studentForm.getName());
            currentStudent.setUniversityGroup(studentForm.getUniversityGroup());
            User user = userService.findUserByEmail(currentStudent.getEmail());
            user.setLogin(studentForm.getEmail());
            userService.updateUser(user.getId(), user);
            currentStudent.setEmail(studentForm.getEmail());
            studentService.updateStudent(currentStudent.getId(), currentStudent);
        }
        return "redirect:/diplomnic";
    }

    @GetMapping("/diplomnic/done")
    public String diplomnicDone(Model model) {
        Student student = studentService.getStudentByEmail(getCurrentUser().getLogin());
        List<TaskAssignment> taskToFind = student.getAssignments();
        String type = "done";
        model.addAttribute("type", type);
        model.addAttribute("task", findTaskByStatus(taskToFind, status.done));
        return "/diplomnic/diplomnic";
    }
    @GetMapping("/diplomnic/pass")
    public String diplomnicPass(Model model) {
        Student student = studentService.getStudentByEmail(getCurrentUser().getLogin());
        List<TaskAssignment> taskToFind = student.getAssignments();
        String type = "pass";
        model.addAttribute("type", type);
        model.addAttribute("task", findTaskByStatus(taskToFind, status.pass));
        return "/diplomnic/diplomnic";
    }
    @GetMapping("/diplomnic/inProgress")
    public String diplomnicInProgress(Model model) {
        Student student = studentService.getStudentByEmail(getCurrentUser().getLogin());
        List<TaskAssignment> taskToFind = student.getAssignments();
        String type = "inProgress";
        model.addAttribute("type", type);
        model.addAttribute("task", findTaskByStatus(taskToFind, status.inProgress));
        return "/diplomnic/diplomnic";
    }
    public List<TaskAssignment> findTaskByStatus(List<TaskAssignment> tasks, status status){
        List<TaskAssignment> task = new ArrayList<>();
        for(TaskAssignment assignment : tasks) {
            if(assignment.getStatus().equals(status)){
                task.add(assignment);
            }
        }
        return task;
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


    @GetMapping("/diplomnic/calendar")
    public String calendar(Model model){
        Student student = studentService.getStudentByEmail(getCurrentUser().getLogin());
        LocalDate date = LocalDate.now();
        addTasks(student.getAssignments(), date, model);
        addDays(date, model);
        model.addAttribute("date", date);
        model.addAttribute("month", getMonthName(date));
        return "/diplomnic/calendar";
    }
    @GetMapping("/diplomnic/calendar/update")
    public String previousMonth(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, Model model) {
        Student student = studentService.getStudentByEmail(getCurrentUser().getLogin());
        addDays(date, model);
        addTasks(student.getAssignments(), date, model);
        model.addAttribute("date", date);
        model.addAttribute("month", getMonthName(date));
        return "/diplomnic/calendar";
    }

    @GetMapping("suggestedThemes")
    public String suggestedThemes(Model model) {
        Supervisor supervisor = supervisorService.findSupervisorByEmail(getCurrentUser().getLogin());
        List<Theme> themes = themeService.getThemesBySupervisorId(supervisor.getId());
        List<Theme> suggestedThemes = new ArrayList<>();
        for(Theme theme : themes){
            if(theme.getStatus().equals(status.suggested)){
                suggestedThemes.add(theme);
            }
        }
        model.addAttribute("suggestedThemes", suggestedThemes);
        return "/diplomnic/theme/suggestedThemes";
    }

    @GetMapping("/diplomnic/groupOfStudent")
    public String groupOfStudent(Model model){
        Student student = studentService.getStudentByEmail(getCurrentUser().getLogin());
        List<StudentGroup> studentGroups = student.getGroups();
        model.addAttribute("studentGroups", studentGroups);
        return "/diplomnic/group/groupOfStudent";
    }

    public void addAuthToModel(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_STUDENT"))) {
            Student student = studentService.getStudentByEmail(getCurrentUser().getLogin());
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
            model.addAttribute("themeENG", themeENG);
            model.addAttribute("student", student);
        }
        else {
            model.addAttribute("supervisor", supervisorService.findSupervisorByEmail(getCurrentUser().getLogin()));
        }
    }
    public void addTasks(List<TaskAssignment> taskAssignments, LocalDate date, Model model) {
        List<TaskAssignment> assignmentList = new ArrayList<>();
        for(TaskAssignment assignment : taskAssignments) {
            if(assignment.getTask().getDeadline().getMonth().equals(date.getMonth()) && assignment.getTask().getDeadline().getYear() == date.getYear()) {
                assignmentList.add(assignment);
            }
        }
        model.addAttribute("assignments", assignmentList);
    }
    public void addDays(LocalDate date, Model model) {
        LocalDate firstDayOfMonth = date.withDayOfMonth(1);
        int daysInMonth = date.getMonth().length(date.isLeapYear());
        int firstDayOfWeek = firstDayOfMonth.getDayOfWeek().getValue();
        List<Integer> calendarDays = new ArrayList<>();
        for (int i = 1; i < firstDayOfWeek; i++) {
            calendarDays.add(0);
        }
        for (int i = 1; i <= daysInMonth; i++) {
            calendarDays.add(i);
        }
        model.addAttribute("calendarDays", calendarDays);
    }

    public String getMonthName(LocalDate date){
        String[] monthNames = {  "Грудень","Січень", "Лютий", "Березень", "Квітень", "Травень", "Червень", "Липень", "Серпень", "Вересень", "Жовтень", "Листопад", "Грудень"};
        return monthNames[date.getMonth().getValue()];
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
