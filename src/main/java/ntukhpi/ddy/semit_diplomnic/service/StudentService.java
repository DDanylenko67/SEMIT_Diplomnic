package ntukhpi.ddy.semit_diplomnic.service;

import ntukhpi.ddy.semit_diplomnic.entity.Student;
import ntukhpi.ddy.semit_diplomnic.entity.Supervisor;

import java.util.List;

public interface StudentService {
    List<Student> getAllStudents();
    Student getStudentById(Long id);
    Student getStudentByName(String name);
    Student getStudentByEmail(String email);
    Student saveStudent(Student student);
    Student updateStudent(Long id, Student student);
    List<Student> getStudentsBySupervisor(Supervisor supervisor);
    void deleteStudentById(Long id);

}
