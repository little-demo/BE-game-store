package antran.project.Repository;

import antran.project.Entity.Transaction;
import antran.project.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}