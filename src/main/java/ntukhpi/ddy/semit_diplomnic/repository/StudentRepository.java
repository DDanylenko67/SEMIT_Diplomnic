package ntukhpi.ddy.semit_diplomnic.repository;

import ntukhpi.ddy.semit_diplomnic.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Student findByName(String name);
    Student findByEmail(String email);
}
