package ntukhpi.ddy.semit_diplomnic.service;

import ntukhpi.ddy.semit_diplomnic.entity.Student;
import ntukhpi.ddy.semit_diplomnic.entity.Task;
import ntukhpi.ddy.semit_diplomnic.entity.TaskAssignment;

import java.util.List;

public interface TaskAssignmentService {
    List<TaskAssignment> getAllTaskAssignments();
    TaskAssignment getTaskAssignmentById(Long id);
    TaskAssignment saveTaskAssignment(TaskAssignment task);
    TaskAssignment updateTaskAssignment(Long id, TaskAssignment task);
    void deleteTaskAssignmentById(Long id);
    public TaskAssignment mergeTaskAssignment(TaskAssignment taskAssignment);
    List<TaskAssignment> getTaskAssignmentByStudentAndTask(Student student, Task task);
    void deleteTaskAssignmentByStudentAndTask(Student student, Task task);
}
