package ntukhpi.ddy.semit_diplomnic.service;

import ntukhpi.ddy.semit_diplomnic.entity.Student;
import ntukhpi.ddy.semit_diplomnic.entity.Task;

import java.util.List;

public interface TaskService {
    List<Task> getAllTasks();
    List<Task> getAllTasksByStudent(Student student);
    Task getTaskById(Long id);
    Task saveTask(Task task);
    Task updateTask(Long id, Task task);
    void deleteTaskById(Long id);
}
