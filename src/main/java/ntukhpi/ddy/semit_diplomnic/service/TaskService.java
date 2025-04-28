package ntukhpi.ddy.semit_diplomnic.service;

import ntukhpi.ddy.semit_diplomnic.entity.Student;
import ntukhpi.ddy.semit_diplomnic.entity.Supervisor;
import ntukhpi.ddy.semit_diplomnic.entity.Task;

import java.time.LocalDate;
import java.util.List;

public interface TaskService {
    List<Task> getAllTasks();
    Task getTaskById(Long id);
    Task saveTask(Task task);
    Task updateTask(Long id, Task task);
    Task getTaskByDateAndDescriptionAndTitle(LocalDate date, String description, String title);
    void deleteTaskById(Long id);
    List<Task> getTasksBySupervisor(Supervisor supervisor);

}
