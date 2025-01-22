package ntukhpi.ddy.semit_diplomnic.service.impl;

import ntukhpi.ddy.semit_diplomnic.entity.Message;
import ntukhpi.ddy.semit_diplomnic.entity.Student;
import ntukhpi.ddy.semit_diplomnic.entity.Supervisor;
import ntukhpi.ddy.semit_diplomnic.repository.MessageRepository;
import ntukhpi.ddy.semit_diplomnic.service.MessageService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageImpl implements MessageService {

    private MessageRepository messageRepository;

    public MessageImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    @Override
    public List<Message> getMessagesBySenderStudentAndReceiverSupervisor(Student student, Supervisor supervisor) {
        return messageRepository.findMessagesByStudentSenderAndSupervisorReceiver(student, supervisor);
    }

    @Override
    public List<Message> getMessagesBySenderSupervisorAndReceiverStudent(Supervisor supervisor, Student student) {
        return messageRepository.findMessagesBySupervisorSenderAndStudentReceiver(supervisor, student);
    }

    @Override
    public Message getMessageById(Long id) {
        return messageRepository.findById(id).orElse(null);
    }

    @Override
    public Message saveMessage(Message message) {
        return messageRepository.save(message);
    }

    @Override
    public Message updateMessage(Long id, Message message) {
        message.setId(id);
        return messageRepository.save(message);
    }

    @Override
    public void deleteMessageById(Long id) {
        messageRepository.deleteById(id);
    }
}
