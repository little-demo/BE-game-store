package antran.project.DTO.Request;

import antran.project.Entity.CardEffectBinding;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

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

    List<EffectAssignment> effects; // ✅ danh sách effect và timing

    @Data
    public static class EffectAssignment {
        Long effectId;
        CardEffectBinding.EffectTiming timing;
    }
}
