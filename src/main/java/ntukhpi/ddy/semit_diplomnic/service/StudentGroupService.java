package ntukhpi.ddy.semit_diplomnic.service;

import ntukhpi.ddy.semit_diplomnic.entity.StudentGroup;
import ntukhpi.ddy.semit_diplomnic.entity.Supervisor;

import java.util.List;

public interface StudentGroupService {
    List<StudentGroup> getAllStudentGroups();
    List<StudentGroup> getStudentsGroupBySupervisor(Supervisor supervisor);
    StudentGroup getStudentGroupByName(String groupName);
    StudentGroup getStudentGroupById(Long id);
    StudentGroup saveStudentGroup(StudentGroup studentGroup);
    StudentGroup updateStudentGroup(Long id, StudentGroup studentGroup);
    void deleteStudentGroupById(Long id);
}
