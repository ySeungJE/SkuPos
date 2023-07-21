package springFinal.POS.domain.User.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springFinal.POS.domain.User.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLoginId(String loginId);
}
