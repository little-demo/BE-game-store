package antran.project.DTO.Response;

import antran.project.Entity.CardEffect;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CardEffectResponse {
    Long id;
    String name;
    int value;
    CardEffect.Type type;
    CardEffect.Target target;
}
