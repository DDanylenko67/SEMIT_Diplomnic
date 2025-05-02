package ntukhpi.ddy.semit_diplomnic.controllers;

import ntukhpi.ddy.semit_diplomnic.entity.Message;
import ntukhpi.ddy.semit_diplomnic.entity.Student;
import ntukhpi.ddy.semit_diplomnic.entity.Supervisor;
import ntukhpi.ddy.semit_diplomnic.entity.TaskAssignment;
import ntukhpi.ddy.semit_diplomnic.service.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/diplomnic")
public class MessageRestController {

    private final UserService userService;
    private final StudentGroupService studentGroupService;
    private final SupervisorService supervisorService;
    private final TaskAssignmentService taskAssignmentService;
    private final StudentService studentService;
    private final MessageService messageService;
    public MessageRestController(UserService userService, StudentGroupService studentGroupService, StudentService studentService,
                               SupervisorService supervisorService, TaskAssignmentService taskAssignmentService, MessageService messageService) {
        this.userService = userService;
        this.studentGroupService = studentGroupService;
        this.supervisorService = supervisorService;
        this.taskAssignmentService = taskAssignmentService;
        this.studentService = studentService;
        this.messageService = messageService;
    }

    public static class MessageDTO {
        public Long taskAssignmentId;
        public String messageText;
        public boolean isStudentSender;
    }

    @PostMapping("/sendMessage")
    public void sendMessage(@RequestBody MessageDTO message) {
        TaskAssignment taskAssignment = taskAssignmentService.getTaskAssignmentById(message.taskAssignmentId);
        Student student = taskAssignment.getStudent();
        Supervisor supervisor = taskAssignment.getTask().getSupervisor();
        Message messageToSave = new Message(message.messageText,  LocalDateTime.now(), message.isStudentSender, supervisor, student, taskAssignment);
        messageService.saveMessage(messageToSave);
    }
}