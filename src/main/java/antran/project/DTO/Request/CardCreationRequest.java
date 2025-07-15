package antran.project.DTO.Request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CardCreationRequest {
    String name;
    String description;
    String cardType;
    String imageUrl;
    String overallImageUrl;
    int mana;
    int attack;
    int health;
    BigDecimal marketPrice;
}
