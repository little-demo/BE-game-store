package antran.project.DTO.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransactionResponse {
    Long id;
    String transactionCode;
    String buyerName;
    String sellerName;
    String cardName;
    String cardImageUrl;
    int quantity;
    BigDecimal amount;
    LocalDateTime transactionDate;

    String transactionType; // "BUY" or "SELL"
}
