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
    String name;
    int value;
    Type type;
    Target target;
    String animationId;

    @OneToMany(mappedBy = "effect")
    List<CardEffectBinding> cardEffectBindings;

    public enum Type
    {
        Damage,
        Heal,
        Draw,
        Summon,
        Taunt,
        Buff,
    /*Silence,
    Destroy,
    Transform,*/
    }
    public enum Target
    {
        None,
        Self,
        Enemy,
        CurrentMinion,
        All,
        AllAlly,
        AllEnemy,
        AllMinions,
        AllEnemyMinions,
        AllAllyMinions,
        RandomAllyMinion,
        RandomEnemyMinion,
        ChosenMinion,
        ChosenTarget
    }
}
