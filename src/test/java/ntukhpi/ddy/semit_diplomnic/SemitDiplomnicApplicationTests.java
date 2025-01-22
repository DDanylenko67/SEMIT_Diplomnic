package ntukhpi.ddy.semit_diplomnic;

import ntukhpi.ddy.semit_diplomnic.entity.*;
import ntukhpi.ddy.semit_diplomnic.enums.groupType.groupType;
import ntukhpi.ddy.semit_diplomnic.enums.status.status;
import ntukhpi.ddy.semit_diplomnic.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

	@Test
	void addSupervisor() {
		UserDto userDto = new UserDto("Дмитро Едуардович Двухглавов", "dmytro.dvukhhlavov@khpi.edu.ua", "admin123","Доктор наук", "Доцент");
		userService.saveUserSupervisor(userDto);
	}
	@Test
	void addStudentGroup() {
		StudentGroup studentGroup = new StudentGroup("Бакалаври-2025", groupType.bachelor, supervisorService.getSupervisorById(1L));
		studentGroupService.saveStudentGroup(studentGroup);
	}

	@Test
	void addStudent(){
		StudentGroup studentGroup = studentGroupService.getStudentGroupById(1L);
		UserDto student1 = new UserDto("Даниленко Денис Юрійович", "denisdanilenko12321@gmail.com", "КН-221в",
				supervisorService.getSupervisorById(1L), studentGroup, "password");
		userService.saveUserStudent(student1);
		Student student = studentService.getStudentByName(student1.getName());
		studentGroupService.addStudentInGroup(student, studentGroup);
		List<Student> students = studentGroupService.getStudentsByStudentGroupName(studentGroup.getGroupName());
		for (Student s : students) {
			System.out.println(s.getName());
		}
	}
	@Test
	void addStudents(){
		StudentGroup studentGroup = studentGroupService.getStudentGroupById(1L);
		UserDto student1 = new UserDto("Кирило Буряк Сергійович", "kirilbyriy123@gmail.com", "КН-221б",
				supervisorService.getSupervisorById(1L), studentGroup, "password");
		UserDto student2 = new UserDto("Валентин Вітайлович Литовченко", "madvalik213@gmail.com", "КН-221в",
				supervisorService.getSupervisorById(1L), studentGroup, "password");
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
		List<Student> students = studentGroupService.getStudentsByStudentGroupName(studentGroup.getGroupName());
		for (Student s : students) {
			System.out.println(s.getName());
		}
	}

	@Test
	void updateStudent() {
		StudentGroup studentGroup = studentGroupService.getStudentGroupById(1L);
		Student student = studentService.getStudentByName("Валентин Вітайлович Литовченко");
		student.setName("Валентин Литовченко Вітайлович");
		studentGroup.setGroupName("Бакалаври 2024-2025");
		studentGroupService.updateStudentGroup(studentGroup.getId(), studentGroup);
		studentService.updateStudent(student.getId(), student);
	}

	@Test
	void addTask(){
		Student student = studentService.getStudentById(1L);
		Supervisor supervisor = supervisorService.getSupervisorById(1L);
		Task task = new Task("Титульні аркуші", "Створити титульні аруші для пояснювальної записки", supervisor, student);
		taskService.saveTask(task);
	}
	@Test
	void updateTask(){
		Task task = taskService.getTaskById(1L);
		task.setDescription("Створити титульні аркуші для пояснювальної записки");
		task.setStatus(status.rejected);
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
		theme.setComment("Гарна тема, намагайся зробити як можна раніше");
		themeService.updateTheme(theme.getId(), theme);
	}
	@Test
	void addMessageFromStudent(){
		Student student = studentService.getStudentById(1L);
		Supervisor supervisor = supervisorService.getSupervisorById(1L);
		Task task = taskService.getTaskById(1L);
		Message message = new Message("Добрий день, а є приклади титульних аркушів?", LocalDateTime.now(), task, supervisor, student);
		messageService.saveMessage(message);
	}
	@Test
	void addMessageFromSupervisor(){
		Student student = studentService.getStudentById(1L);
		Supervisor supervisor = supervisorService.getSupervisorById(1L);
		Task task = taskService.getTaskById(1L);
		Message message = new Message("Добрий день, у репозиторії НТУ ХПІ є прикладти титульних аркушів", LocalDateTime.now(), task, student, supervisor);
		messageService.saveMessage(message);
	}
}
