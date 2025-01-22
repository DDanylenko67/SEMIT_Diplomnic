package ntukhpi.ddy.semit_diplomnic.service;

import ntukhpi.ddy.semit_diplomnic.entity.Student;
import ntukhpi.ddy.semit_diplomnic.entity.StudentGroup;
import ntukhpi.ddy.semit_diplomnic.entity.Supervisor;

import java.util.List;

public interface StudentGroupService {
    List<StudentGroup> getAllStudentGroups();
    List<StudentGroup> getStudentsGroupBySupervisor(Supervisor supervisor);
    List<Student> getStudentsByStudentGroupName(String studentGroupName);
    StudentGroup getStudentGroupByName(String groupName);
    StudentGroup getStudentGroupById(Long id);
    StudentGroup saveStudentGroup(StudentGroup studentGroup);
    StudentGroup updateStudentGroup(Long id, StudentGroup studentGroup);
    StudentGroup addStudentInGroup(Student student, StudentGroup studentGroup);
    StudentGroup addStudentsInGroup(List<Student> students, StudentGroup studentGroup);
    void deleteStudentGroupById(Long id);
}
