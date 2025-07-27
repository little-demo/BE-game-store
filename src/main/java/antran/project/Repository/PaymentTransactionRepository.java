package antran.project.Repository;

import antran.project.Entity.PaymentTransaction;
import antran.project.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Long> {
    PaymentTransaction findByOrderId(String id);
    List<PaymentTransaction> findAllByUserOrderByCreatedAtDesc(User user);
    List<PaymentTransaction> findAllByOrderByCreatedAtDesc();

}