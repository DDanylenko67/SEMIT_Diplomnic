package ntukhpi.ddy.semit_diplomnic.repository;

import ntukhpi.ddy.semit_diplomnic.entity.Supervisor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupervisorRepository extends JpaRepository<Supervisor, Long> {
    Supervisor findByName(String name);
    Supervisor findByEmail(String email);
}
