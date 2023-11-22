package springFinal.POS.domain.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import springFinal.POS.domain.order.Order;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByOrderUid(String orderUid);
//    @Query("select o from Order o" +
//            " left join fetch o.payment p" +
//            " left join fetch o.member m" +
//            " where o.orderUid = :orderUid")
//    Optional<Order> findOrderAndPaymentAndMember(String orderUid);
//
//    @Query("select o from Order o" +
//            " left join fetch o.payment p" +
//            " where o.orderUid = :orderUid")
//    Optional<Order> findOrderAndPayment(String orderUid);
}
