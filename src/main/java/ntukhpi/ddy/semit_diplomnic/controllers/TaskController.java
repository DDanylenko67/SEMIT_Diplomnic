package ntukhpi.ddy.semit_diplomnic.controllers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ntukhpi.ddy.semit_diplomnic.entity.*;
import ntukhpi.ddy.semit_diplomnic.entity.tools.FolderDeleter;
import ntukhpi.ddy.semit_diplomnic.enums.groupType.groupType;
import ntukhpi.ddy.semit_diplomnic.enums.status.status;
import ntukhpi.ddy.semit_diplomnic.service.*;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
    private static final String UPLOAD_DIR = "uploads/";



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
    @GetMapping("/diplomnic/supervisor/createTask")
    public String createTask(Model model) {
        addAttribute(model);
        Task task = new Task();
        model.addAttribute("task", task);
        return "/diplomnic/createTask";
    }

    @GetMapping("/diplomnic/supervisor/updateTask/{id}")
    public String updateTask(@PathVariable Long id,@RequestParam("group_id") Long groupId, Model model) {
        Task task  = taskService.getTaskById(id);
        addAttribute(model);
        model.addAttribute("task", task);
        model.addAttribute("taskDeadline", task.getDeadline());
        model.addAttribute("groupId", groupId);
        return "/diplomnic/group/updateTask";
    }
    @PostMapping("/diplomnic/supervisor/saveTask")
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
    @GetMapping("/diplomnic/supervisor/deleteTask/{id}")
    public String deleteTask(@PathVariable Long id, Model model){
        Task task = taskService.getTaskById(id);
        List<StudentGroup> originalGroups = new ArrayList<>(task.getStudentGroups());
        for (StudentGroup group : originalGroups) {
            deleteAssignments(task, group);
            deleteStudentGroup(task, group);
        }
        taskService.updateTask(task.getId(), task);
        taskService.deleteTaskById(task.getId());
        return  "redirect:/diplomnic";
    }
    @PostMapping("/diplomnic/supervisor/updateSavedTask/{id}")
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
        if (selectedBachelors != null && !selectedBachelors.isEmpty()) {
            for (Long selectedGroup : selectedBachelors) {
                groups.add(studentGroupService.getStudentGroupById(selectedGroup));
            }
        }
        if(selectedMasters != null && !selectedMasters.isEmpty()){
            for (Long selectedGroup : selectedMasters) {
                groups.add(studentGroupService.getStudentGroupById(selectedGroup));
            }
        }

        if (!taskDB.getStudentGroups().equals(groups)) {
            List<StudentGroup> originalGroups = new ArrayList<>(taskDB.getStudentGroups());
            for (StudentGroup group : originalGroups) {
                if (!groups.contains(group)) {
                    deleteAssignments(taskDB, group);
                    deleteStudentGroup(taskDB, group);
                }
            }
            if (!groups.isEmpty()) {
                for (StudentGroup group : groups) {
                    if (!taskDB.getStudentGroups().contains(group)) {
                        taskDB.getStudentGroups().add(group);
                        addAssignments(group, taskDB);
                    }
                }
            }
        }
        if(groupId != null && groupId == 0){
            return "redirect:/diplomnic";
        }
        return  "redirect:/diplomnic/supervisor/groupTasks/" + groupId;
    }


    @PostMapping("/diplomnic/supervisor/saveTasksFromFiles")
    @ResponseBody
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file,
                                                   @RequestParam(value = "selectedBachelors", required = false) List<Long> selectedBachelors,
                                                   @RequestParam(value = "selectedMasters", required = false) List<Long> selectedMasters) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Файл порожній");
        }
        List<StudentGroup> lisfOfGroups = new ArrayList<>();
        if(selectedBachelors != null && !selectedBachelors.isEmpty()){
            for (Long selectedGroup : selectedBachelors) {
                lisfOfGroups.add(studentGroupService.getStudentGroupById(selectedGroup));
            }
        }
        if(selectedMasters != null && !selectedMasters.isEmpty()){
            for (Long selectedGroup : selectedMasters) {
                lisfOfGroups.add(studentGroupService.getStudentGroupById(selectedGroup));
            }
        }
        Supervisor supervisor = supervisorService.findSupervisorByEmail(getCurrentUser().getLogin());
        String filename = file.getOriginalFilename();
        try (InputStream fis = file.getInputStream();
             XWPFDocument document = new XWPFDocument(fis)) {

            List<XWPFTable> tables = document.getTables();
            for (XWPFTable table : tables) {
                String headerText = table.getRows().get(0).getTableCells().get(0).getText().toLowerCase().trim();
                if (headerText.equals("назва етапа") || headerText.equals("назва етапу")) {
                    for (XWPFTableRow row : table.getRows()) {
                        String rowText =  row.getTableCells().get(0).getText().toLowerCase().trim();
                        if (row.getTableCells().size() == 3 &&
                                !rowText.equals("назва етапа") &&
                                !rowText.equals("назва етапу")) {
                            StringBuilder title = new StringBuilder();
                            List<XWPFParagraph> paragraphs = row.getTableCells().get(0).getParagraphs();
                            for (XWPFParagraph paragraph : paragraphs) {
                                title.append(paragraph.getText());
                                title.append("\t");
                            }
                            LocalDate dateOfTask = readDate(row.getTableCells().get(1).getText());
                            StringBuilder description = new StringBuilder();
                            paragraphs = row.getTableCells().get(1).getParagraphs();
                            for (XWPFParagraph paragraph : paragraphs) {
                                description.append(paragraph.getText());
                                description.append(" ");
                            }
                            description.append("\n");
                            paragraphs = row.getTableCells().get(2).getParagraphs();
                            for (XWPFParagraph paragraph : paragraphs) {
                                description.append(paragraph.getText());
                                description.append("\n");
                            }
                            if(dateOfTask != null){
                                Task task = new Task();
                                task.setDeadline(dateOfTask);
                                task.setTitle(title.toString());
                                task.setDescription(description.toString());
                                task.setSupervisor(supervisor);
                                task.setDateOfCreate(LocalDate.now());
                                taskService.saveTask(task);
                                if (selectedBachelors != null ) {
                                    for (Long selectedGroup : selectedBachelors) {
                                        addAssignments(studentGroupService.getStudentGroupById(selectedGroup), task);
                                    }
                                }
                                if(selectedMasters != null){
                                    for (Long selectedGroup : selectedMasters) {
                                        addAssignments(studentGroupService.getStudentGroupById(selectedGroup), task);
                                    }
                                }
                            }
                        }
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok("Файл успішно завантажено: " + filename);
    }

    public LocalDate readDate(String date) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        date = date.trim().toLowerCase().replace("\n", " ");

        LocalDate fromDate = null;

        if (date.contains("з") && (date.contains("до") || date.contains("по"))) {
            String[] parts = date.split("з|до|по");
            if (parts.length >= 2) {
                fromDate = LocalDate.parse(parts[1].trim(), inputFormatter);
            }
        } else if (date.contains("з")) {
            fromDate = LocalDate.parse(date.replace("з", "").trim(), inputFormatter);
        } else if (date.contains("до") || date.contains("по")) {
            fromDate = LocalDate.parse(date.replace("до", "").replace("по", "").trim(), inputFormatter);
        }

        return fromDate;
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
                List<TaskAssignment> assignmentsToRemove = new ArrayList<>();
                for (TaskAssignment assignment : student.getAssignments()) {
                    if (assignment.getTask().getId().equals(task.getId())) {
                        assignmentsToRemove.add(assignment);
                    }
                }

                for (TaskAssignment assignment : assignmentsToRemove) {
                    student.getAssignments().remove(assignment);
                    String directoryPath = UPLOAD_DIR + "/" + assignment.getStudent().getEmail() + "/assignment" + assignment.getId();
                    String archivePath = UPLOAD_DIR + "/" + assignment.getStudent().getEmail();
                    FolderDeleter.deleteFolder(directoryPath);
                    FolderDeleter.deleteFolder(archivePath);
                    task.getAssignments().remove(assignment);
                    studentService.updateStudent(student.getId(), student);
                    taskService.updateTask(task.getId(), task);
                    taskAssignmentService.deleteTaskAssignmentById(assignment.getId());
                }
            }
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
