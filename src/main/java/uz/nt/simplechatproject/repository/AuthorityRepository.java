package uz.nt.simplechatproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.nt.simplechatproject.model.Authorities;

import java.util.Set;

public interface AuthorityRepository extends JpaRepository<Authorities, Integer> {
    Set<Authorities> findAllByAuthorityIn(Set<String> auth);
}
