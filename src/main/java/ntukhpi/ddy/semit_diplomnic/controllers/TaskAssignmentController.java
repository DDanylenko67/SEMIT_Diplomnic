package ntukhpi.ddy.semit_diplomnic.controllers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ntukhpi.ddy.semit_diplomnic.entity.Message;
import ntukhpi.ddy.semit_diplomnic.entity.Student;
import ntukhpi.ddy.semit_diplomnic.entity.TaskAssignment;
import ntukhpi.ddy.semit_diplomnic.entity.User;
import ntukhpi.ddy.semit_diplomnic.enums.status.status;
import ntukhpi.ddy.semit_diplomnic.service.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
public class TaskAssignmentController {
    @PersistenceContext
    private EntityManager entityManager;
    private final TaskService taskService;
    private final UserService userService;
    private final StudentService studentService;
    private final SupervisorService supervisorService;
    private final StudentGroupService studentGroupService;
    private final TaskAssignmentService taskAssignmentService;
    private final MessageService messageService;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    String minDate = LocalDate.now().plusDays(1).format(formatter);
    String maxDate = LocalDate.now().plusYears(1).format(formatter);
    private static final String UPLOAD_DIR = "uploads/";

    public TaskAssignmentController(final TaskService taskService, final StudentService studentService,
                          SupervisorService supervisorService, StudentGroupService studentGroupService,
                          UserService userService, TaskAssignmentService taskAssignmentService, MessageService messageService) {
        this.taskService = taskService;
        this.studentService = studentService;
        this.supervisorService = supervisorService;
        this.studentGroupService = studentGroupService;
        this.userService = userService;
        this.taskAssignmentService = taskAssignmentService;
        this.messageService = messageService;
    }
    @GetMapping("/diplomnic/calendar/assignmentDate")
    public String diplomnicAssignmentDate(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, Model model){
        Student student = studentService.getStudentByEmail(getCurrentUser().getLogin());
        addAssignments(student, date, model);
        model.addAttribute("student", student);
        return "diplomnic/assignment/assignmentDate";
    }
    @GetMapping("/diplomnic/taskAssignment/{id}")
    public String diplomnicTaskAssignment(@PathVariable Long id, Model model){
        TaskAssignment taskAssignment = taskAssignmentService.getTaskAssignmentById(id);
        String directoryPath = UPLOAD_DIR + "/" + taskAssignment.getStudent().getEmail() + "/assignment" + taskAssignment.getId();
        long fileCount = 0;
        try {
             fileCount = Files.list(Paths.get(directoryPath))
                    .filter(Files::isRegularFile)
                    .count();
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Message> messages = messageService.getMessagesByAssignment(taskAssignment);
        model.addAttribute("messages", messages);
        model.addAttribute("count", fileCount);
        model.addAttribute("taskAssignment", taskAssignment);
        return "/diplomnic/assignment/taskAssignment";
    }


    @PostMapping("/diplomnic/saveTaskAssignment/{id}")
    public String saveTaskAssignment(@RequestParam("file") MultipartFile[] files, Model model, @PathVariable Long id){
        Student student = studentService.getStudentByEmail(getCurrentUser().getLogin());
        TaskAssignment taskAssignment = taskAssignmentService.getTaskAssignmentById(id);

        try{
            Path uploadPath = Path.of(UPLOAD_DIR + "/" + student.getEmail() + "/assignment" + taskAssignment.getId() );
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            for(MultipartFile file : files){
                if(!file.isEmpty()){
                    Path filePath = uploadPath.resolve(file.getOriginalFilename());
                    Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                }
            }
            taskAssignment.setStatus(status.checking);
            taskAssignmentService.updateTaskAssignment(taskAssignment.getId(), taskAssignment);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return "redirect:/diplomnic";
    }

    public void addAssignments(Student student, LocalDate date, Model model){
        List<TaskAssignment> assignments = student.getAssignments();
        List<TaskAssignment> temp = new ArrayList<>();
        for(TaskAssignment assignment : assignments){
           if(assignment.getTask().getDeadline().getMonth().equals(date.getMonth()) &&
                    assignment.getTask().getDeadline().getDayOfMonth() == date.getDayOfMonth() &&
                            assignment.getTask().getDeadline().getYear() == date.getYear() ){
               temp.add(assignment);
           }
        }
        model.addAttribute("task", temp);
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
