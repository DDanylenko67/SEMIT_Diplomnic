package ntukhpi.ddy.semit_diplomnic.controllers;

import ntukhpi.ddy.semit_diplomnic.entity.*;
import ntukhpi.ddy.semit_diplomnic.enums.groupType.groupType;
import ntukhpi.ddy.semit_diplomnic.enums.status.status;
import ntukhpi.ddy.semit_diplomnic.service.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static ntukhpi.ddy.semit_diplomnic.entity.SecureRandomCodeGenerator.generateSecureRandomCode;

@Controller
public class groupController {
    private final UserService userService;
    private final StudentGroupService studentGroupService;
    private final SupervisorService supervisorService;
    private final StudentService studentService;
    private final ThemeService themeService;
    private final TaskAssignmentService taskAssignmentService;
    private final TaskService taskService;
    public groupController(UserService userService, StudentGroupService studentGroupService,
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
    @GetMapping("/createGroup")
    public String createGroup(Model model) {
        Supervisor supervisor = supervisorService.findSupervisorByEmail(getCurrentUser().getLogin());
        StudentGroup newGroup = new StudentGroup("", groupType.bachelor, supervisor, "");
        model.addAttribute("group", newGroup);
        return "/diplomnic/createGroup";

    }
    @GetMapping("groupInfo/{id}")
    public String groupInfo(@PathVariable Long id, Model model) {
        StudentGroup group = studentGroupService.getStudentGroupById(id);
        model.addAttribute("group", group);
        return "/diplomnic/groupInfo";
    }
    @GetMapping("/updateGroup/{id}")
    public String updateGroup(@PathVariable Long id, Model model) {
        StudentGroup group = studentGroupService.getStudentGroupById(id);
        model.addAttribute("group", group);
        return "/diplomnic/updateGroup";
    }

    @GetMapping("/progress/{id}")
    public String progress(Model model, @PathVariable Long id) {
        StudentGroup group = studentGroupService.getStudentGroupById(id);
        model.addAttribute("group", group);
        List<Student> students = group.getStudents();
        model.addAttribute("students", students);
        return "/diplomnic/group/progress";
    }

    @GetMapping("/groupTasks/{id}")
    public String groupTasks(Model model, @PathVariable Long id) {
        StudentGroup group = studentGroupService.getStudentGroupById(id);
        List<Task> tasks = group.getTasks();
        model.addAttribute("tasks", tasks);
        model.addAttribute("group", group);
        return "/diplomnic/group/groupTasks";
    }


    @GetMapping("/groupList/{id}")
    public String groupList(Model model, @PathVariable Long id) {
        StudentGroup group = studentGroupService.getStudentGroupById(id);
        List<Student> students = group.getStudents();
        model.addAttribute("group", group);
        model.addAttribute("students", students);
        return "/diplomnic/group/groupList";
    }

    @GetMapping("/diplomnic/addStudent/{group_id}")
    public String addStudent(Model model, @PathVariable Long group_id) {
        StudentGroup studentGroup = studentGroupService.getStudentGroupById(group_id);
        List<StudentGroup> studentGroups = new ArrayList<>();
        studentGroups.add(studentGroup);
        String themeUA = "";
        Student student = new Student("", "", "", studentGroups);
        System.out.println(student.getGroup().get(0));
        model.addAttribute("student", student);
        model.addAttribute("themeUA", themeUA);
        return "/diplomnic/group/createStudent";
    }

    @PostMapping("/diplomnic/saveStudent")
    public String saveStudent(@ModelAttribute("student") Student student,
                              @RequestParam(value = "themeUA", required = false) String themeUA, @RequestParam("group_id") Long groupId) {
        Supervisor supervisor = supervisorService.findSupervisorByEmail(getCurrentUser().getLogin());
        StudentGroup studentGroup = studentGroupService.getStudentGroupById(groupId);
        List<StudentGroup> studentGroups = new ArrayList<>();
        studentGroups.add(studentGroup);
        if(studentService.getStudentByEmail(student.getEmail()) == null) {
            UserDto studentToSave = new UserDto(student.getName(), student.getEmail(),
                    student.getUniversityGroup(), studentGroups, "password");
            userService.saveUserStudent(studentToSave);
            Student studentFromDB = studentService.getStudentByEmail(student.getEmail());
            studentGroup.getStudents().add(studentFromDB);
            studentFromDB.getGroups().add(studentGroup);
            studentGroupService.updateStudentGroup(studentGroup.getId(), studentGroup);
            if(themeUA != null) {
                Theme theme = new Theme(themeUA, status.checking, supervisor, studentFromDB);
                themeService.saveTheme(theme);
            }
            addTask(studentGroup, studentFromDB);
        }
        if(!studentGroup.getStudents().contains(studentService.getStudentByEmail(student.getEmail()))) {
            Student studentFromDB = studentService.getStudentByEmail(student.getEmail());
            studentGroup.getStudents().add(studentFromDB);
            studentFromDB.getGroups().add(studentGroup);
            studentGroupService.updateStudentGroup(studentGroup.getId(), studentGroup);
            addTask(studentGroup, studentFromDB);
        }
        return  "redirect:/groupList/" + groupId;
    }

    public void addTask(StudentGroup studentGroup, Student studentFromDB) {
        List<Task> tasks = studentGroup.getTasks();
        for(Task task : tasks) {
            TaskAssignment taskAssignment = new TaskAssignment(task, studentFromDB, studentGroup,status.inProgress);
            taskAssignmentService.saveTaskAssignment(taskAssignment);
            List<TaskAssignment> taskAssignmentsFromDB = taskAssignmentService.getTaskAssignmentByStudentAndTask(studentFromDB, task);
            task.getAssignments().addAll(taskAssignmentsFromDB);
            studentFromDB.getAssignments().addAll(taskAssignmentsFromDB);
            taskService.updateTask(task.getId(), task);
        }
        studentService.updateStudent(studentFromDB.getId(), studentFromDB);
    }

    @PostMapping("diplomnic/saveStudents/{id}")
    public String saveStudents(Model model, @PathVariable Long id, @RequestParam("file") MultipartFile file){
        Supervisor supervisor = supervisorService.findSupervisorByEmail(getCurrentUser().getLogin());
        StudentGroup studentGroup = studentGroupService.getStudentGroupById(id);
        updateGroupByExcel(file, supervisor, studentGroup);
        return  "redirect:/groupList/" + id;
    }


    @PostMapping("/diplomnic/updateGroup")
    public String updateOldGroup(@ModelAttribute StudentGroup group,
                                 @RequestParam("file") MultipartFile file,
                                 @RequestParam Long id,
                                 @ModelAttribute("group") StudentGroup groupToSave, Model model) {
        Supervisor supervisor = supervisorService.findSupervisorByEmail(getCurrentUser().getLogin());
        StudentGroup studentGroup = studentGroupService.getStudentGroupById(id);
        studentGroup.setGroupName(groupToSave.getGroupName());
        studentGroup.setGroupType(groupToSave.getGroupType());
        studentGroupService.updateStudentGroup(id, studentGroup);
        if(!file.isEmpty()){
            readExcel(file, supervisor, studentGroup);
        }
        return "redirect:/diplomnic";
    }
    @PostMapping("/diplomnic/saveGroup")
    public String saveGroup(@ModelAttribute StudentGroup group,
                              @RequestParam("file") MultipartFile file,
                              @ModelAttribute("group") StudentGroup groupToSave,
                              Model model) {
        Supervisor supervisor = supervisorService.findSupervisorByEmail(getCurrentUser().getLogin());
        groupToSave.setSupervisor(supervisor);
        groupToSave.setCode(generateSecureRandomCode(10));
        studentGroupService.saveStudentGroup(groupToSave);
        if(!file.isEmpty()){
            readExcel(file, supervisor, groupToSave);
        }

        return "redirect:/diplomnic";

    }

    @GetMapping("/deleteStudent/{id}")
    public String deleteStudentFromGroup(Model model, @PathVariable Long id, @RequestParam("group_id") Long groupId){
        StudentGroup studentGroup = studentGroupService.getStudentGroupById(groupId);
        Student student = studentService.getStudentById(id);
        studentGroup.getStudents().removeIf(s -> s.equals(student));
        student.getGroup().remove(studentGroup);
        studentGroupService.updateStudentGroup(studentGroup.getId(), studentGroup);
        List<Task> tasks = studentGroup.getTasks();
        for (Task task : tasks) {
            Iterator<TaskAssignment> iterator = task.getAssignments().iterator();
            while (iterator.hasNext()) {
                TaskAssignment taskAssignment = iterator.next();
                if (taskAssignment.getStudent().getId() == (student.getId())) {
                    TaskAssignment taskAssignmentFromDB = taskAssignmentService.getTaskAssignmentById(taskAssignment.getId());
                    iterator.remove();
                    student.getAssignments().remove(taskAssignmentFromDB);
                    taskAssignmentService.deleteTaskAssignmentById(taskAssignmentFromDB.getId());
                }
            }
        }
        return "redirect:/groupList/" + groupId;
    }

    @GetMapping("/studentAssigments/{id}")
    public String studentAssignment(Model model, @PathVariable Long id, @RequestParam("group_id") Long groupId){
        StudentGroup studentGroup = studentGroupService.getStudentGroupById(groupId);
        Student student = studentService.getStudentById(id);
        for(TaskAssignment taskAssignment : student.getAssignmentByGroup(groupId)){
            System.out.println(taskAssignment.getTask().getTitle());
        }
        model.addAttribute("group", studentGroup);
        model.addAttribute("student", student);
        return "/diplomnic/group/assignment";
    }




    private void updateGroupByExcel(MultipartFile file, Supervisor supervisor, StudentGroup studentGroup1){
        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            StudentGroup studentGroup = studentGroupService.getStudentGroupByName(studentGroup1.getGroupName(), supervisor);
            List<Student> students = readCells(sheet, studentGroup, supervisor);
            for(Task task: studentGroup.getTasks()){
                for(Student student : students){
                    TaskAssignment taskAssignment = new TaskAssignment(task, student, studentGroup,status.inProgress);
                    taskAssignmentService.saveTaskAssignment(taskAssignment);
                    List<TaskAssignment> taskAssignmentsFromDB = taskAssignmentService.getTaskAssignmentByStudentAndTask(studentService.getStudentByEmail(student.getEmail()), task);
                    task.getAssignments().addAll(taskAssignmentsFromDB);
                    student.getAssignments().addAll(taskAssignmentsFromDB);
                }
            }
            List<Student> temp = studentGroupService.getStudentsByStudentGroupName(studentGroup1.getGroupName(), supervisor);
            temp.addAll(students);
            studentGroup.setStudents(temp);
            studentGroupService.updateStudentGroup(studentGroup.getId(), studentGroup);
        } catch (IOException e) {
            System.out.println("Помилка під час читання файлу");
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

    private void readExcel(MultipartFile file, Supervisor supervisor, StudentGroup studentGroup1){
        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            StudentGroup studentGroup = studentGroupService.getStudentGroupByName(studentGroup1.getGroupName(), supervisor);
            List<Student> temp = readCells(sheet, studentGroup, supervisor);
            List<Student> students = studentGroupService.getStudentsByStudentGroupName(studentGroup1.getGroupName(), supervisor);
            students.addAll(temp);
            studentGroup.setStudents(students);
            studentGroupService.updateStudentGroup(studentGroup.getId(), studentGroup);
        } catch (IOException e) {
            System.out.println("Помилка під час читання файлу");
        }
    }
    private List<Student> readCells(Sheet sheet, StudentGroup studentGroup, Supervisor supervisor) {
        List<Student> students = new ArrayList<>();
        for (Row row : sheet) {
            String themeEng = "";
            String themeUA = "";
            if(row.getCell(4) != null){
                themeUA = row.getCell(4).getStringCellValue();
            }
            if(row.getCell(5) != null){
                themeEng = row.getCell(5).getStringCellValue();
            }
            if(row.getCell(0) != null
                    && row.getCell(1) != null
                    && row.getCell(2) != null) {
                String studentName = row.getCell(0).getStringCellValue();
                String email = row.getCell(1).getStringCellValue();
                String universityGroup = row.getCell(2).getStringCellValue();
                if(userService.findUserByEmail(email) == null){
                    if (email.endsWith("@khpi.edu.ua") || email.endsWith("@gmail.com")){
                        List<StudentGroup> groups = new ArrayList<>();
                        groups.add(studentGroup);
                        UserDto student = new UserDto(studentName, email, universityGroup, groups, "password");
                        userService.saveUserStudent(student);
                        Student studentFromDB = studentService.getStudentByEmail(email);
                        if(!themeUA.equals("") || !themeEng.equals("")){
                            if(!themeUA.equals(" ") || !themeEng.equals(" ")){
                                Theme theme = new Theme(themeUA, themeEng, status.checking, supervisor, studentFromDB);
                                themeService.saveTheme(theme);
                            }
                        }
                        students.add(studentService.getStudentByEmail(email));
                    }
                }
                else{
                    if(!studentGroup.getStudents().contains(studentService.getStudentByEmail(email))){
                        students.add(studentService.getStudentByEmail(email));
                    }
                }
            }
        }
        return students;
    }
}
