package ntukhpi.ddy.semit_diplomnic;

import ntukhpi.ddy.semit_diplomnic.entity.*;
import ntukhpi.ddy.semit_diplomnic.entity.tools.UserDto;
import ntukhpi.ddy.semit_diplomnic.enums.groupType.groupType;
import ntukhpi.ddy.semit_diplomnic.enums.status.status;
import ntukhpi.ddy.semit_diplomnic.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class SemitDiplomnicApplicationTests {

	@Autowired
	private StudentService studentService;
	@Autowired
	private SupervisorService supervisorService;
	@Autowired
	private StudentGroupService studentGroupService;
	@Autowired
	private UserService userService;
	@Autowired
	private ThemeService themeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private MessageService messageService;
	@Autowired
	private TaskAssignmentService taskAssignmentService;

	@Test
	void addSupervisor() {
		UserDto userDto = new UserDto("Дмитро Едуардович Двухглавов", "dmytro.dvukhhlavov@khpi.edu.ua", "admin123", "Доцент", "Доктор наук");
		userService.saveUserSupervisor(userDto);
	}
	@Test
	void addStudentGroup() {
		StudentGroup studentGroup = new StudentGroup("Бакалаври 2025", groupType.bachelor, supervisorService.getSupervisorById(1L));
		studentGroupService.saveStudentGroup(studentGroup);
		StudentGroup studentGroup1 = new StudentGroup("Магістри-2025-2026", groupType.master, supervisorService.getSupervisorById(1L));
		studentGroupService.saveStudentGroup(studentGroup1);
	}

	@Test
	void addStudent(){
		List<StudentGroup> groups = new ArrayList<>();
		groups.add(studentGroupService.getStudentGroupById(1L));
		groups.add(studentGroupService.getStudentGroupById(2L));
		UserDto student1 = new UserDto("Даниленко Денис Юрійович", "danilenkodenis12321@gmail.com", "КН-221в",
				 groups, "password");
		userService.saveUserStudent(student1);
		Student student = studentService.getStudentByName(student1.getName());
		studentGroupService.addStudentInGroup(student, studentGroupService.getStudentGroupById(1L));
		studentGroupService.addStudentInGroup(student, studentGroupService.getStudentGroupById(2L));

	}
	@Test
	@Transactional
	void getGroups(){
		Student student = studentService.getStudentByName("Даниленко Денис Юрійович");
		List<StudentGroup> groups = student.getGroup();
		for(StudentGroup group : groups){
			System.out.println(group.getGroupName());
		}
	}
	@Test
	void addStudents(){
		StudentGroup studentGroup = studentGroupService.getStudentGroupById(1L);
		List<StudentGroup> groups = new ArrayList<>();
		groups.add(studentGroup);
		UserDto student1 = new UserDto("Буряк Кирило Сергійович", "kirilbyriy123@gmail.com", "КН-221б",
				 groups, "password");
		UserDto student2 = new UserDto("Ілля Євсієнко В'ячеславович", "madvalik213@gmail.com", "КН-221в",
				groups, "password");
		userService.saveUserStudent(student1);
		userService.saveUserStudent(student2);
		List<Student> students = new ArrayList<>();
		students.add(studentService.getStudentByName(student1.getName()));
		students.add(studentService.getStudentByName(student2.getName()));
		studentGroupService.addStudentsInGroup(students, studentGroup);
	}

	@Test
	void getStudents() {
		StudentGroup studentGroup = studentGroupService.getStudentGroupById(1L);
		Supervisor supervisor = supervisorService.getSupervisorById(1L);
		List<Student> students = studentGroupService.getStudentsByStudentGroupName(studentGroup.getGroupName(), supervisor);
		for (Student s : students) {
			System.out.println(s.getName());
		}
	}

	@Test
	void updateStudent() {
		StudentGroup studentGroup = studentGroupService.getStudentGroupById(1L);
		Student student = studentService.getStudentByName("Ілля Євсієнко В'ячеславович");
		student.setName("Євсієнко Ілля В'ячеславович");
		studentGroup.setGroupName("Бакалаври 2024-2025");
		studentGroupService.updateStudentGroup(studentGroup.getId(), studentGroup);
		studentService.updateStudent(student.getId(), student);
	}

	@Test
	void saveTask(){
		LocalDate date = LocalDate.now().plusMonths(1);
		List<StudentGroup> groups = new ArrayList<>();
		Supervisor supervisor = supervisorService.getSupervisorById(1L);
		StudentGroup group = studentGroupService.getStudentGroupById(1L);
		groups.add(group);
		Task task = new Task("Титульні аркуші", "Створити титульні аркуші для пояснювальної записки", supervisor, date, groups);
		taskService.saveTask(task);
	}
	@Test
	void saveTasksGroup(){
		StudentGroup group = studentGroupService.getStudentGroupById(1L);
		Task task = taskService.getTaskById(2L);
		group.getTasks().add(task);
		studentGroupService.updateStudentGroup(group.getId(), group);
	}
	@Test
	void addTaskAssigment(){
		Student student = studentService.getStudentById(1L);
		StudentGroup studentGroup = studentGroupService.getStudentGroupById(1L);
		Task task = taskService.getTaskById(1L);
		System.out.println(task.getTitle());
		TaskAssignment taskAssignment = new TaskAssignment(task, student, studentGroup,status.inProgress);
		taskAssignmentService.saveTaskAssignment(taskAssignment);
		student.getAssignments().add(taskAssignment);
		task.getAssignments().add(taskAssignment);
		taskService.updateTask(task.getId(), task);
		studentService.updateStudent(student.getId(), student);
	}
	
	@Test
	void updateTask(){
		Task task = taskService.getTaskById(1L);
		task.setDescription("Створити титульні аркуші для пояснювальної записки");
		taskService.updateTask(task.getId(), task);
	}

	@Test
	void deleteStudentFromGroup(){
		Student student = studentService.getStudentByEmail("hguev123@khpi.edu.ua");
		StudentGroup studentGroup =  studentGroupService.getStudentGroupById(1L);
		studentGroup.getStudents().remove(student);
		studentGroupService.updateStudentGroup(studentGroup.getId(), studentGroup);
	}

	@Test
	void deleteTask(){
		Task task = taskService.getTaskById(1L);
		for(TaskAssignment taskAssignment : task.getAssignments()){
			if(taskAssignment != null){
				taskAssignmentService.deleteTaskAssignmentById(taskAssignment.getId());
			}
		}
		List<StudentGroup> studentGroups = new ArrayList<>(task.getStudentGroups());
		for(StudentGroup studentGroup : studentGroups){
			if(studentGroup != null){
				studentGroupService.deleteStudentGroupById(studentGroup.getId());
			}
		}
		task.getAssignments().clear();
		task.getStudentGroups().clear();
		taskService.updateTask(task.getId(), task);
		taskService.deleteTaskById(task.getId());
	}

	@Transactional
	@Test
	void deleteGroupFromTask(){
		Task task = taskService.getTaskById(5L);
		StudentGroup studentGroup = studentGroupService.getStudentGroupById(1L);
		List<Student> students = studentGroup.getStudents();

		for(Student student : students){
			for(TaskAssignment taskAssignment : student.getAssignments()){
				if(taskAssignment.getTask().getId().equals(task.getId())){
					taskAssignmentService.deleteTaskAssignmentById(taskAssignment.getId());
					task.getAssignments().remove(taskAssignment);
				}
			}
		}

		studentGroup.getTasks().remove(task);
		task.getStudentGroups().remove(studentGroup);
		studentGroupService.updateStudentGroup(studentGroup.getId(), studentGroup);
		taskService.updateTask(task.getId(), task);
	}
	@Test
	void addTheme(){
		Student student = studentService.getStudentById(1L);
		Supervisor supervisor = supervisorService.getSupervisorById(1L);
		Theme theme = new Theme("Проєктування та розробка системи контроля виконання завдань в ході дипломного проектування",
				"Design and development of a system for the performance of tasks in the course of diploma",
				status.checking,supervisor, student);
		themeService.saveTheme(theme);
	}
	@Test
	void updateTheme(){
		Student student = studentService.getStudentById(1L);
		Theme theme = themeService.getThemeByStudent(student);
		theme.setStatus(status.done);
		themeService.updateTheme(theme.getId(), theme);
	}
	@Test
	void addMessageFromStudent(){
		Student student = studentService.getStudentById(1L);
		Supervisor supervisor = supervisorService.getSupervisorById(1L);
		Task task = taskService.getTaskById(1L);
		Message message = new Message("Добрий день, а є приклади титульних аркушів?", LocalDateTime.now(), true, supervisor, student, taskAssignmentService.getTaskAssignmentById(1L));
		messageService.saveMessage(message);
	}
	@Test
	void addMessageFromSupervisor(){
		Student student = studentService.getStudentById(1L);
		Supervisor supervisor = supervisorService.getSupervisorById(1L);
		Task task = taskService.getTaskById(1L);
		Message message = new Message("Добрий день, у репозиторії НТУ ХПІ є прикладти титульних аркушів", LocalDateTime.now(), false, supervisor, student,  taskAssignmentService.getTaskAssignmentById(1L));
		messageService.saveMessage(message);
	}
}
