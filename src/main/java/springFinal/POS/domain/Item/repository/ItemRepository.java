package springFinal.POS.domain.Item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springFinal.POS.domain.Item.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Optional<Item> findByName(String name);
    <T> List<T> findAllBy(Class<T> type);

    List<Item> findAll();

    //== 특정 컬럼 조회 인터페이스 ==//
    interface ItemMapping {
        String getName();
        Boolean getExist();
    }
}
