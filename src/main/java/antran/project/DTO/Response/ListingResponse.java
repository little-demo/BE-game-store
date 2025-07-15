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
    Long cardId;
    String cardName;
    String cardImageUrl;
    BigDecimal sellingPrice;
    int quantity;
    LocalDateTime postedAt;
    LocalDateTime soldAt;
    public String getStatus() {
        return soldAt != null ? "Đã bán" : "Đang bán";
    }
}
