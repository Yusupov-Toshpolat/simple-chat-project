package uz.nt.simplechatproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.nt.simplechatproject.model.Chat;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Integer> {
    boolean existsByName(String name);
    List<Chat> findAllByIdInOrderByCreatedAt(List<Integer> ids);
}
