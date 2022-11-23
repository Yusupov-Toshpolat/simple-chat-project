package uz.nt.simplechatproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.nt.simplechatproject.model.ChatUsers;

import java.util.List;

public interface ChatUsersRepository extends JpaRepository<ChatUsers, Integer> {
    boolean existsByChatIdAndUserId(Integer chatId, Integer userId);
    List<ChatUsers> findAllByUserId(Integer userId);
}
