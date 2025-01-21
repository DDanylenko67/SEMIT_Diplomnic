package ntukhpi.ddy.semit_diplomnic.service;

import ntukhpi.ddy.semit_diplomnic.entity.Student;

import java.util.List;

public interface StudentService {
    List<Student> getAllStudents();
    Student getStudentById(Long id);
    Student getStudentByName(String name);
    Student saveStudent(Student student);
    Student updateStudent(Long id, Student student);
    void deleteStudentById(Long id);

}
