package ntukhpi.ddy.semit_diplomnic.service;

import ntukhpi.ddy.semit_diplomnic.entity.Message;
import ntukhpi.ddy.semit_diplomnic.entity.Student;
import ntukhpi.ddy.semit_diplomnic.entity.Supervisor;

import java.util.List;

public interface MessageService {
    List<Message> getAllMessages();
    List<Message> getMessagesBySenderStudentAndReceiverSupervisor(Student student, Supervisor supervisor);
    List<Message> getMessagesBySenderSupervisorAndReceiverStudent(Supervisor supervisor, Student student);
    Message getMessageById(Long id);
    Message saveMessage(Message message);
    Message updateMessage(Long id, Message message);
    void deleteMessageById(Long id);
}
