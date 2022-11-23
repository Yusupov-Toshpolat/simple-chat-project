package uz.nt.simplechatproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.nt.simplechatproject.model.Message;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer> {
    List<Message> findAllByChatId(Integer chatId);
}
