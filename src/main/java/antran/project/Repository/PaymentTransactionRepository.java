package antran.project.Repository;

import antran.project.DTO.Response.RevenueByDateDTO;
import antran.project.Entity.PaymentTransaction;
import antran.project.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Long> {
    PaymentTransaction findByOrderId(String id);
    List<PaymentTransaction> findAllByUserOrderByCreatedAtDesc(User user);
    List<PaymentTransaction> findAllByOrderByCreatedAtDesc();

    @Query("SELECT CAST(p.createdAt AS date), SUM(p.amountVND) " +
            "FROM PaymentTransaction p " +
            "WHERE p.status = antran.project.Entity.PaymentTransaction.PaymentStatus.SUCCESS " +
            "GROUP BY CAST(p.createdAt AS date) " +
            "ORDER BY CAST(p.createdAt AS date)")
    List<Object[]> getRevenueByDateRaw();
}