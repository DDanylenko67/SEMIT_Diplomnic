package ntukhpi.ddy.semit_diplomnic.controllers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ntukhpi.ddy.semit_diplomnic.entity.*;
import ntukhpi.ddy.semit_diplomnic.enums.groupType.groupType;
import ntukhpi.ddy.semit_diplomnic.enums.status.status;
import ntukhpi.ddy.semit_diplomnic.service.*;
import org.hibernate.sql.ast.tree.update.Assignment;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Controller
public class TaskController {
    @PersistenceContext
    private EntityManager entityManager;
    private final TaskService taskService;
    private final UserService userService;
    private final StudentService studentService;
    private final SupervisorService supervisorService;
    private final StudentGroupService studentGroupService;
    private final TaskAssignmentService taskAssignmentService;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    String minDate = LocalDate.now().plusDays(1).format(formatter);
    String maxDate = LocalDate.now().plusYears(1).format(formatter);


    public TaskController(final TaskService taskService, final StudentService studentService,
                          SupervisorService supervisorService, StudentGroupService studentGroupService,
                          UserService userService, TaskAssignmentService taskAssignmentService) {
        this.taskService = taskService;
        this.studentService = studentService;
        this.supervisorService = supervisorService;
        this.studentGroupService = studentGroupService;
        this.userService = userService;
        this.taskAssignmentService = taskAssignmentService;
    }
    @GetMapping("/createTask")
    public String createTask(Model model) {
        addAttribute(model);
        Task task = new Task();
        model.addAttribute("task", task);
        return "/diplomnic/createTask";
    }

    @GetMapping("/updateTask/{id}")
    public String updateTask(@PathVariable Long id,@RequestParam("group_id") Long groupId, Model model) {
        Task task  = taskService.getTaskById(id);
        addAttribute(model);
        model.addAttribute("task", task);
        model.addAttribute("taskDeadline", task.getDeadline());
        model.addAttribute("groupId", groupId);
        return "/diplomnic/group/updateTask";
    }
    @PostMapping("/saveTask")
    public String saveTask(
            @ModelAttribute("task") Task taskToSave,
            @RequestParam(required = false) List<Long> selectedBachelors,
            @RequestParam(required = false) List<Long> selectedMasters,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") LocalDate date,
            Model model){
        taskToSave.setSupervisor(supervisorService.findSupervisorByEmail(getCurrentUser().getLogin()));
        taskToSave.setDateOfCreate(LocalDate.now());
        taskToSave.setDeadline(date);
        taskService.saveTask(taskToSave);
        if (selectedBachelors != null ) {
            for (Long selectedGroup : selectedBachelors) {
                addAssignments(studentGroupService.getStudentGroupById(selectedGroup), taskToSave);
            }
        }
        if(selectedMasters != null){
            for (Long selectedGroup : selectedMasters) {
                addAssignments(studentGroupService.getStudentGroupById(selectedGroup), taskToSave);
            }
        }
        return "redirect:/diplomnic";
    }
    @GetMapping("/deleteTask/{id}")
    public String deleteTask(@PathVariable Long id, @RequestParam("group_id") Long groupId, Model model){
        Task task = taskService.getTaskById(id);
        deleteAssignments(task);
        deleteGroups(task);
        task.getAssignments().clear();
        task.getStudentGroups().clear();
        taskService.updateTask(task.getId(), task);
        taskService.deleteTaskById(task.getId());
        return  "redirect:/groupTasks/" + groupId;
    }
    @PostMapping("/updateSavedTask/{id}")
    public String updateSavedTask(@PathVariable Long id,
                             @ModelAttribute("task") Task taskToSave,
                             @RequestParam(required = false) List<Long> selectedBachelors,
                             @RequestParam(required = false) List<Long> selectedMasters,
                             @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") LocalDate date, @RequestParam("group_id") Long groupId, Model model){
        Task taskDB = taskService.getTaskById(id);
        taskDB.setDateOfUpdate(LocalDate.now());
        taskDB.setDeadline(date);
        taskDB.setDescription(taskToSave.getDescription());
        taskDB.setTitle(taskToSave.getTitle());
        List<StudentGroup> groups = new ArrayList<>();
        if (selectedBachelors != null) {
            for (Long selectedGroup : selectedBachelors) {
                groups.add(studentGroupService.getStudentGroupById(selectedGroup));
            }
        }
        if(selectedMasters != null){
            for (Long selectedGroup : selectedMasters) {
                groups.add(studentGroupService.getStudentGroupById(selectedGroup));
            }
        }
        if(groups.isEmpty()){
            return "redirect:/deleteTask/"+taskDB.getId();
        }
        if(!taskDB.getStudentGroups().equals(groups)){
            for (StudentGroup group : taskDB.getStudentGroups()) {
                if(!groups.contains(group)){
                    deleteAssignments(taskDB, group);
                    deleteStudentGroup(taskDB, group);
                }
            }
            for (StudentGroup group : groups) {
                if(!taskDB.getStudentGroups().contains(group)){
                    addAssignments(group, taskDB);
                }
            }
        }
        return  "redirect:/groupTasks/" + groupId;
    }

    public void deleteStudentGroup(Task task, StudentGroup studentGroup){
        task.getStudentGroups().remove(studentGroup);
        studentGroup.getTasks().remove(task);
        taskService.updateTask(task.getId(), task);
        studentGroupService.updateStudentGroup(studentGroup.getId(), studentGroup);
    }

    public void deleteAssignments(Task task, StudentGroup studentGroup) {
        if (!studentGroup.getStudents().isEmpty()) {
            for (Student student : studentGroup.getStudents()) {
                Iterator<TaskAssignment> iterator = student.getAssignments().iterator();
                while (iterator.hasNext()) {
                    TaskAssignment taskAssignment = iterator.next();
                    if (taskAssignment.getTask().getId().equals(task.getId())) {
                        iterator.remove();
                        taskAssignmentService.deleteTaskAssignmentById(taskAssignment.getId());
                        task.getAssignments().remove(taskAssignment);
                        taskService.updateTask(task.getId(), task);
                    }
                }
            }
        }
    }
    public void deleteAssignments(Task task){
        List<TaskAssignment> assignments = new ArrayList<>(task.getAssignments());
        for (TaskAssignment assignment : assignments) {
            TaskAssignment managedAssignment = taskAssignmentService.getTaskAssignmentById(assignment.getId());
            taskAssignmentService.deleteTaskAssignmentById(managedAssignment.getId());
        }
    }
    public void deleteGroups(Task task){
        List<StudentGroup> groupsCopy = new ArrayList<>(task.getStudentGroups());
        for (StudentGroup gr : groupsCopy) {
            gr.getTasks().remove(task);
            studentGroupService.updateStudentGroup(gr.getId(), gr);
        }
    }

    public void addAttribute(Model model){
        Supervisor supervisor = supervisorService.findSupervisorByEmail(getCurrentUser().getLogin());
        List<Student> students = studentService.getStudentsBySupervisor(supervisor);
        List<StudentGroup> studentGroups = studentGroupService.getStudetsGroupBySupervisor(supervisor);
        List<StudentGroup> studentGroupsBec = new ArrayList<>();
        List<StudentGroup> studentGroupsMas = new ArrayList<>();
        for(StudentGroup studentGroup : studentGroups){
            if(studentGroup.getGroupType().toString().equals(groupType.bachelor.getDisplayName())){
                studentGroupsBec.add(studentGroup);
            }else {
                studentGroupsMas.add(studentGroup);
            }
        }
        model.addAttribute("minDate", minDate);
        model.addAttribute("maxDate", maxDate);
        model.addAttribute("bachelors", studentGroupsBec);
        model.addAttribute("masters", studentGroupsMas);
    }


    public void addAssignments(StudentGroup studentGroup, Task task) {
        List<Student> students = new ArrayList<>(studentGroup.getStudents());
        for (Student student : students) {
            TaskAssignment taskAssignment = new TaskAssignment(task, student, studentGroup,status.inProgress);
            taskAssignmentService.saveTaskAssignment(taskAssignment);

            task.getAssignments().add(taskAssignment);
            student.getAssignments().add(taskAssignment);

            taskService.updateTask(task.getId(), task);
            studentService.updateStudent(student.getId(), student);
            if (!studentGroup.getTasks().contains(task)) {
                studentGroup.getTasks().add(task);
                studentGroupService.updateStudentGroup(studentGroup.getId(), studentGroup);
            }
        }
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
