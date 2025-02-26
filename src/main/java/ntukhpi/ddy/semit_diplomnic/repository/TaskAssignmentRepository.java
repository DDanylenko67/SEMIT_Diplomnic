package ntukhpi.ddy.semit_diplomnic.repository;

import ntukhpi.ddy.semit_diplomnic.entity.Student;
import ntukhpi.ddy.semit_diplomnic.entity.Task;
import ntukhpi.ddy.semit_diplomnic.entity.TaskAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskAssignmentRepository extends JpaRepository<TaskAssignment, Long> {
    TaskAssignment findTaskAssignmentById(Long id);
    List<TaskAssignment> getTaskAssignmentByStudentAndTask(Student student, Task task);
    void deleteTaskAssignmentsByStudentAndTask(Student student, Task task);
}
