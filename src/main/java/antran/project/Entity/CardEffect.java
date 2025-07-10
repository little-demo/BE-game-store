package antran.project.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Table(name = "card_effects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CardEffect {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private int value;
    private Type type;
    private Target target;

    @OneToMany(mappedBy = "effect")
    List<CardEffectBinding> cardEffectBindings;

    public enum Type {
        DAMAGE,
        HEAL,
        DRAW,
        SUMMON,
        TAUNT,
        BUFF,
        // SILENCE,
        // DESTROY,
        // TRANSFORM
    }

    public enum Target {
        NONE,
        SELF,
        ENEMY,
        FRIENDLY,
        ALL,
        ALL_MINIONS,
        RANDOM_ENEMY_MINION,
        CHOSEN_MINION,
        CHOSEN_CHARACTER
    }
}
