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
import java.util.List;

import static ntukhpi.ddy.semit_diplomnic.entity.SecureRandomCodeGenerator.generateSecureRandomCode;

@Controller
public class groupController {
    private final UserService userService;
    private final StudentGroupService studentGroupService;
    private final SupervisorService supervisorService;
    private final StudentService studentService;
    private final ThemeService themeService;
    public groupController(UserService userService, StudentGroupService studentGroupService,
                           SupervisorService supervisorService, StudentService studentService,
                           ThemeService themeService) {
        this.userService = userService;
        this.studentGroupService = studentGroupService;
        this.supervisorService = supervisorService;
        this.studentService = studentService;
        this.themeService = themeService;
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
            StudentGroup studentGroup = studentGroupService.getStudentGroupByName(studentGroup1.getGroupName());
            List<Student> students = readCells(sheet, studentGroup, supervisor);
            List<Student> temp = studentGroupService.getStudentsByStudentGroupName(studentGroup1.getGroupName());
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
            if(row.getCell(5) != null){
                themeEng = row.getCell(5).getStringCellValue();
            }
            if(row.getCell(0) != null
                    && row.getCell(1) != null
                    && row.getCell(2) != null
                    && row.getCell(4) != null) {
                String studentName = row.getCell(0).getStringCellValue();
                String email = row.getCell(1).getStringCellValue();
                String universityGroup = row.getCell(2).getStringCellValue();
                String themeUA = row.getCell(4).getStringCellValue();
                if(userService.findUserByEmail(email) == null){
                    List<StudentGroup> groups = new ArrayList<>();
                    groups.add(studentGroup);
                    UserDto student = new UserDto(studentName, email, universityGroup, groups, "password");
                    userService.saveUserStudent(student);
                    Student studentFromDB = studentService.getStudentByEmail(email);
                    Theme theme = new Theme(themeUA, themeEng, status.checking, supervisor, studentFromDB);
                    themeService.saveTheme(theme);
                    students.add(studentService.getStudentByEmail(email));
                }
            }
        }
        return students;
    }
}
