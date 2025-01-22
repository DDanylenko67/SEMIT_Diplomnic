package ntukhpi.ddy.semit_diplomnic.service.impl;

import ntukhpi.ddy.semit_diplomnic.entity.Supervisor;
import ntukhpi.ddy.semit_diplomnic.repository.SupervisorRepository;
import ntukhpi.ddy.semit_diplomnic.service.SupervisorService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupervisorImpl implements SupervisorService{
    private SupervisorRepository supervisorRepository;

    public SupervisorImpl(SupervisorRepository supervisorRepository) {
        super();
        this.supervisorRepository = supervisorRepository;
    }

    @Override
    public List<Supervisor> getAllSupervisors() {
        return supervisorRepository.findAll();
    }

    @Override
    public Supervisor saveSupervisor(Supervisor supervisor) {
        return supervisorRepository.save(supervisor);
    }

    @Override
    public Supervisor getSupervisorById(Long id) {
        return supervisorRepository.findById(id).orElse(null);
    }

    @Override
    public Supervisor updateSupervisor(Long id, Supervisor supervisor) {
        supervisor.setId(id);
        return supervisorRepository.save(supervisor);
    }

    @Override
    public void deleteSupervisorById(Long id) {
        supervisorRepository.deleteById(id);
    }
}
