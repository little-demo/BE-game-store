package antran.project.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "card_effect_bindings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CardEffectBinding {
    @Id
    @GeneratedValue
    Long id;

    @ManyToOne
    Card card;

    @ManyToOne
    CardEffect effect;

    @Enumerated(EnumType.STRING)
    EffectTiming timing; // ON_PLAY, ON_START, ON_END

    public enum EffectTiming {
        ON_PLAY,
        ON_START,
        ON_END
    }
}
