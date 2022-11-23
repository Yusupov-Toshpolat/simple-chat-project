package uz.nt.simplechatproject.repository.session;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSessionRedisRepository extends CrudRepository<UserSession, String> {
}
