package ntukhpi.ddy.semit_diplomnic.controllers;
import ntukhpi.ddy.semit_diplomnic.entity.Student;
import ntukhpi.ddy.semit_diplomnic.entity.TaskAssignment;
import ntukhpi.ddy.semit_diplomnic.entity.User;
import ntukhpi.ddy.semit_diplomnic.enums.status.status;
import ntukhpi.ddy.semit_diplomnic.service.StudentService;
import ntukhpi.ddy.semit_diplomnic.service.TaskAssignmentService;
import ntukhpi.ddy.semit_diplomnic.service.TaskService;
import ntukhpi.ddy.semit_diplomnic.service.UserService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Controller
public class TaskAssignmentRestController {
    private final UserService userService;
    private final StudentService studentService;
    private final TaskAssignmentService  taskAssignmentService;
    private static final String DIRECTORY = "uploads/";

    public TaskAssignmentRestController(UserService userService, StudentService studentService, TaskAssignmentService taskAssignmentService) {
        this.userService = userService;
        this.studentService = studentService;
        this.taskAssignmentService = taskAssignmentService;
    }
    @GetMapping("/sendMessage")
    public ResponseEntity<String> sendMessage(
            @RequestParam Long taskAssignmentId,
            @RequestParam String messageText,
            @RequestParam boolean isStudentSender) {

        System.out.println("ID: " + taskAssignmentId);
        System.out.println("Text: " + messageText);
        System.out.println("isStudentSender: " + isStudentSender);

        return ResponseEntity.ok("Повідомлення надіслано");
    }
    @GetMapping("/files/download-all/{taskAssignmentId}")
    public ResponseEntity<Resource> downloadAllFiles(@PathVariable Long taskAssignmentId) {
        TaskAssignment taskAssignment = taskAssignmentService.getTaskAssignmentById(taskAssignmentId);
        String archivePath = DIRECTORY + "/" + taskAssignment.getStudent().getEmail();
        String path = DIRECTORY + "/" + taskAssignment.getStudent().getEmail() + "/assignment" + taskAssignment.getId();

        try {
            String zipFileName = taskAssignment.getStudent().getName() + " " + taskAssignment.getTask().getTitle() + ".zip";
            Path zipPath = Paths.get(archivePath, zipFileName);
            try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipPath.toFile()))) {
                Files.list(Paths.get(path)).forEach(file -> {
                    try {
                        if (Files.isRegularFile(file)) {
                            zipOut.putNextEntry(new ZipEntry(file.getFileName().toString()));
                            Files.copy(file, zipOut);
                            zipOut.closeEntry();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }

            Resource fileResource = new FileSystemResource(zipPath);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + URLEncoder.encode(zipFileName, StandardCharsets.UTF_8));
            headers.add(HttpHeaders.CONTENT_TYPE, "application/zip");
            headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(zipPath.toFile().length()));

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(fileResource);

        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    @GetMapping("/tasks/update-status/{taskAssignmentId}/{statusFrom}")
    @ResponseBody
    public ResponseEntity<String> updateTaskStatus(@PathVariable Long taskAssignmentId, @PathVariable String statusFrom) {
        TaskAssignment taskAssignment = taskAssignmentService.getTaskAssignmentById(taskAssignmentId);
        switch (statusFrom) {
            case "rejected":
                taskAssignment.setStatus(status.rejected);
                break;
            case "done":
                taskAssignment.setStatus(status.done);
                break;
            case "checking":
                taskAssignment.setStatus(status.checking);
                break;
            default:
                return ResponseEntity.badRequest().body("Invalid status");
        }

        taskAssignmentService.updateTaskAssignment(taskAssignmentId, taskAssignment);
        return ResponseEntity.ok("Status updated successfully");
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
