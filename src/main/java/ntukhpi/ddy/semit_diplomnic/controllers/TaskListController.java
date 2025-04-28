package ntukhpi.ddy.semit_diplomnic.controllers;

import ntukhpi.ddy.semit_diplomnic.entity.*;
import ntukhpi.ddy.semit_diplomnic.service.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
public class TaskListController {
    private final UserService userService;
    private final StudentGroupService studentGroupService;
    private final SupervisorService supervisorService;
    private final TaskAssignmentService taskAssignmentService;
    private final StudentService studentService;
    private final TaskService taskService;
    private final ThemeService themeService;
    public TaskListController(UserService userService, StudentGroupService studentGroupService, StudentService studentService,
                               SupervisorService supervisorService, TaskAssignmentService taskAssignmentService, ThemeService themeService, TaskService taskService) {
        this.userService = userService;
        this.studentGroupService = studentGroupService;
        this.supervisorService = supervisorService;
        this.taskService = taskService;
        this.taskAssignmentService = taskAssignmentService;
        this.studentService = studentService;
        this.themeService = themeService;
    }
    @GetMapping("/diplomnic/listOfTask")
    public String listOfTask(Model model) {
        Supervisor supervisor = supervisorService.findSupervisorByEmail(getCurrentUser().getLogin());
        model.addAttribute("listOfTask", getListOfTask(supervisor));
        return "/diplomnic/task/listOfTask";
    }
    @GetMapping("/diplomnic/supervisorCalendar")
    public String supervisorCalendar(Model model) {
        Supervisor supervisor = supervisorService.findSupervisorByEmail(getCurrentUser().getLogin());
        LocalDate date = LocalDate.now();
        addTasks(getListOfTask(supervisor), date, model);
        addDays(date, model);
        model.addAttribute("date", date);
        model.addAttribute("month", getMonthName(date));
        return "/diplomnic/task/SupervisorCalendar";
    }


    @GetMapping("/diplomnic/supervisorCalendar/update")
    public String previousMonth(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, Model model) {
        Supervisor supervisor = supervisorService.findSupervisorByEmail(getCurrentUser().getLogin());
        addDays(date, model);
        addTasks(getListOfTask(supervisor), date, model);
        model.addAttribute("date", date);
        model.addAttribute("month", getMonthName(date));
        return "/diplomnic/task/SupervisorCalendar";
    }

    @GetMapping("/assignmentOfTask/{id}")
    public String assignmentOfTask(@PathVariable Long id, Model model) {
        Task task = taskService.getTaskById(id);
        model.addAttribute("assignments", task.getAssignments());
        return "/diplomnic/task/assignmentOfTask";
    }

    @GetMapping("/diplomnic/supervisorCalendar/assignmentDate")
    public String diplomnicAssignmentDate(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, Model model){
        Supervisor supervisor = supervisorService.findSupervisorByEmail(getCurrentUser().getLogin());
        addAssignments(supervisor, date, model);
        return "/diplomnic/task/listOfTask";
    }
    public List<Task> getListOfTask(Supervisor supervisor) {
        return taskService.getTasksBySupervisor(supervisor);
    }

    public void addAssignments(Supervisor supervisor, LocalDate date, Model model){
        List<Task> tasks = getListOfTask(supervisor);
        List<Task> temp = new ArrayList<>();
        for(Task task : tasks){
            if(task.getDeadline().getMonth().equals(date.getMonth()) &&
                    task.getDeadline().getDayOfMonth() == date.getDayOfMonth() &&
                    task.getDeadline().getYear() == date.getYear() ){
                temp.add(task);
            }
        }
        model.addAttribute("listOfTask", temp);
    }

    public void addTasks(List<Task> tasks, LocalDate date, Model model) {
        List<Task> taskList = new ArrayList<>();
        for(Task task : tasks) {
            if(task.getDeadline().getMonth().equals(date.getMonth()) && task.getDeadline().getYear() == date.getYear()) {
                taskList.add(task);
            }
        }
        model.addAttribute("tasks", taskList);
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
