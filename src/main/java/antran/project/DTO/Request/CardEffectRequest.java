package antran.project.DTO.Request;

import antran.project.Entity.CardEffect;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CardEffectRequest {
    String name;
    int value;
    CardEffect.Type type;
    CardEffect.Target target;
}
