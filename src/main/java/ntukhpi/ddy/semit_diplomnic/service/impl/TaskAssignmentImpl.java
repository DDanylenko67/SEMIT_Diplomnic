package ntukhpi.ddy.semit_diplomnic.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ntukhpi.ddy.semit_diplomnic.entity.TaskAssignment;
import ntukhpi.ddy.semit_diplomnic.repository.SupervisorRepository;
import ntukhpi.ddy.semit_diplomnic.repository.TaskAssignmentRepository;
import ntukhpi.ddy.semit_diplomnic.service.TaskAssignmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TaskAssignmentImpl implements TaskAssignmentService {
    @PersistenceContext
    private EntityManager entityManager;

    private TaskAssignmentRepository taskAssignmentRepository;

    public TaskAssignmentImpl(TaskAssignmentRepository taskAssignmentRepository) {
        super();
        this.taskAssignmentRepository = taskAssignmentRepository;
    }
    @Override
    public List<TaskAssignment> getAllTaskAssignments() {
        return taskAssignmentRepository.findAll();
    }

    @Override
    public TaskAssignment getTaskAssignmentById(Long id) {
        return taskAssignmentRepository.findTaskAssignmentById(id);
    }

    @Transactional
    @Override
    public TaskAssignment saveTaskAssignment(TaskAssignment task) {
        return taskAssignmentRepository.save(task);
    }

    @Override
    public TaskAssignment updateTaskAssignment(Long id, TaskAssignment task) {
        task.setId(id);
        return taskAssignmentRepository.save(task);
    }

    @Override
    public void deleteTaskAssignmentById(Long id) {
        taskAssignmentRepository.deleteById(id);
    }
    @Transactional
    @Override
    public TaskAssignment mergeTaskAssignment(TaskAssignment taskAssignment) {
        return entityManager.merge(taskAssignment);
    }
}
