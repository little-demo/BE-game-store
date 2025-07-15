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
    String cardName;
    String buyerName;
    String sellerName;
    BigDecimal amount;
    LocalDateTime transactionDate;
}
