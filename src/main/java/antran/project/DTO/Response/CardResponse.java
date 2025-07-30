package antran.project.DTO.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CardResponse {
    Long id;
    String name;
    String description;
    String cardType;
    String imageUrl;
    String overallImageUrl;
    int mana;
    int attack;
    int health;
    BigDecimal marketPrice;

    List<EffectWithTiming> effects; // Thêm danh sách hiệu ứng + thời điểm

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class EffectWithTiming {
        Long effectId;
        String effectName;
        String timing;
    }
}
