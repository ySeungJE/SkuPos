package springFinal.POS.domain.Item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springFinal.POS.domain.Item.Item;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    public Optional<Item> findByName(String name);
}
