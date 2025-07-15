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
    private int value;
    private Type type;
    private Target target;

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
        All,
        AllMinions,
        AllEnemyMinions,
        AllAllyMinions,
        RandomAllyMinion,
        RandomEnemyMinion,
        ChosenMinion,
        ChosenTarget,
    }
}
