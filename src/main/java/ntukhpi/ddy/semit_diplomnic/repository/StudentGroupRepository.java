package ntukhpi.ddy.semit_diplomnic.repository;

import ntukhpi.ddy.semit_diplomnic.entity.Student;
import ntukhpi.ddy.semit_diplomnic.entity.StudentGroup;
import ntukhpi.ddy.semit_diplomnic.entity.Supervisor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentGroupRepository extends JpaRepository<StudentGroup, Long> {
    StudentGroup findByGroupName(String groupName);
    List<StudentGroup> findStudentGroupsBySupervisor(Supervisor supervisor);
}
