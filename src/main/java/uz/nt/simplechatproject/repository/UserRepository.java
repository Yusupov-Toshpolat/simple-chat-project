package uz.nt.simplechatproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.nt.simplechatproject.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByUsername(String username);
    Optional<User> findFirstByUsername(String username);
    boolean existsAllByIdIn(List<Integer> ids);
}
