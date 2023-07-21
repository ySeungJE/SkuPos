package springFinal.POS.domain.Pos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springFinal.POS.domain.Pos.MyPos;

public interface MyPosRepository extends JpaRepository<MyPos, Long> {
}
