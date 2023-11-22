package springFinal.POS.domain.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springFinal.POS.domain.payment.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}