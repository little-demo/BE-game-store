package antran.project.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment_transaction")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;

    String orderId;       // Mã giao dịch VNPay
    Long amountVND;       // Số tiền nạp
    Integer diamonds;     // Số kim cương được quy đổi

    @Enumerated(EnumType.STRING)
    PaymentStatus status; // PENDING, SUCCESS, FAILED

    String transactionNo; // VNPay trả về

    LocalDateTime createdAt;
    LocalDateTime confirmedAt;

    public enum PaymentStatus {
        PENDING, SUCCESS, FAILED
    }
}
