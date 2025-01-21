package ntukhpi.ddy.semit_diplomnic.repository;

import ntukhpi.ddy.semit_diplomnic.entity.Message;
import ntukhpi.ddy.semit_diplomnic.entity.Student;
import ntukhpi.ddy.semit_diplomnic.entity.Supervisor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    Message findBySupervisorSenderAndStudentReceiver(Supervisor supervisorSender, Student studentReceiver);
    Message findByStudentSenderAndSupervisorReceiver(Student studentSender, Supervisor supervisorReceiver);
    List<Message> findMessagesBySupervisorSenderAndStudentReceiver(Supervisor supervisorSender, Student studentReceiver);
    List<Message> findMessagesByStudentSenderAndSupervisorReceiver(Student studentSender, Supervisor supervisorReceiver);
}
