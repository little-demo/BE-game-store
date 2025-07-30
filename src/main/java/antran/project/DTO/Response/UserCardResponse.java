package antran.project.DTO.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCardResponse {
    Long userCardId;
    int quantity;
    int deckQuantity;
    int marketQuantity;

    Long cardId;
    String cardName;
    String cardType;
    String description;
    String imageUrl;
    String overallImageUrl;
    int mana;
    int attack;
    int health;
    BigDecimal marketPrice;
}
