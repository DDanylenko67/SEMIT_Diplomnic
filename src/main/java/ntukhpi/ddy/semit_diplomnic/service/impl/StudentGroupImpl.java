package ntukhpi.ddy.semit_diplomnic.service.impl;

import ntukhpi.ddy.semit_diplomnic.entity.Student;
import ntukhpi.ddy.semit_diplomnic.entity.StudentGroup;
import ntukhpi.ddy.semit_diplomnic.entity.Supervisor;
import ntukhpi.ddy.semit_diplomnic.repository.StudentGroupRepository;
import ntukhpi.ddy.semit_diplomnic.service.StudentGroupService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentGroupImpl implements StudentGroupService {
    private StudentGroupRepository studentGroupRepository;

    public StudentGroupImpl(StudentGroupRepository studentGroupRepository) {
        super();
        this.studentGroupRepository = studentGroupRepository;
    }
    @Override
    public List<StudentGroup> getAllStudentGroups() {
        return studentGroupRepository.findAll();
    }

    @Override
    public List<StudentGroup> getStudentsGroupBySupervisor(Supervisor supervisor) {
        return studentGroupRepository.findStudentGroupsBySupervisor(supervisor);
    }

    @Override
    public List<Student> getStudentsByStudentGroupName(String studentGroupName) {
        StudentGroup studentGroup = studentGroupRepository.findByGroupName(studentGroupName);
        return studentGroup.getStudents();
    }

    @Override
    public StudentGroup getStudentGroupByName(String groupName) {
        return studentGroupRepository.findByGroupName(groupName);
    }

    @Override
    public StudentGroup getStudentGroupById(Long id) {
        return studentGroupRepository.findById(Long.valueOf(id)).orElse(null);
    }

    @Override
    public StudentGroup saveStudentGroup(StudentGroup studentGroup) {
        return studentGroupRepository.save(studentGroup);
    }

    @Override
    public StudentGroup updateStudentGroup(Long id, StudentGroup studentGroup) {
        studentGroup.setId(Long.valueOf(id));
        return saveStudentGroup(studentGroup);
    }

    @Override
    public StudentGroup addStudentInGroup(Student student, StudentGroup studentGroup) {
        StudentGroup group =  studentGroupRepository.findById(studentGroup.getId()).orElse(null);
        if (group != null) {
            group.getStudents().add(student);
            return saveStudentGroup(group);
        }
        return saveStudentGroup(group);
    }

    @Override
    public StudentGroup addStudentsInGroup(List<Student> students, StudentGroup studentGroup) {
        StudentGroup group =  studentGroupRepository.findById(studentGroup.getId()).orElse(null);
        for(Student student : students){
            group.getStudents().add(student);
        }
        return saveStudentGroup(group);
    }

    @Override
    public void deleteStudentGroupById(Long id) {
        studentGroupRepository.deleteById(id);
    }
}
