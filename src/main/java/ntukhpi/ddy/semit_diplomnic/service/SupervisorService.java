package ntukhpi.ddy.semit_diplomnic.service;

import ntukhpi.ddy.semit_diplomnic.entity.Supervisor;

import java.util.List;

public interface SupervisorService {
    List<Supervisor> getAllSupervisors();
    Supervisor saveSupervisor(Supervisor supervisor);
    Supervisor getSupervisorById(Long id);
    Supervisor updateSupervisor(Long id, Supervisor supervisor);
    void deleteSupervisorById(Long id);
}
