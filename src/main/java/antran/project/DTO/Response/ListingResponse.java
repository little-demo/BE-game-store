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
public class ListingResponse {
    Long id;
    String sellerName;
    Long cardId;
    String cardName;
    String cardImageUrl;
    BigDecimal sellingPrice;
    int quantity;
    LocalDateTime postedAt;
    LocalDateTime soldAt;
    String cardType;
    boolean isCancelled = false;
    public String getStatus() {
        if (isCancelled) return "Đã hủy";
        if (quantity == 0) return "Đã bán";
        return "Đang bán";
    }
}
