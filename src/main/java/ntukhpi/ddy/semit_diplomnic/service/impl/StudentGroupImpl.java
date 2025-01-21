package ntukhpi.ddy.semit_diplomnic.service.impl;

import ntukhpi.ddy.semit_diplomnic.entity.StudentGroup;
import ntukhpi.ddy.semit_diplomnic.entity.Supervisor;
import ntukhpi.ddy.semit_diplomnic.repository.StudentGroupRepository;
import ntukhpi.ddy.semit_diplomnic.service.StudentGroupService;

import java.util.List;

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
    public void deleteStudentGroupById(Long id) {
        studentGroupRepository.deleteById(id);
    }
}
