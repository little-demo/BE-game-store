package antran.project.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "cards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    String description;

    @Enumerated(EnumType.STRING)
    cardType cardType;

    String imageUrl;
    String overallImageUrl;
    int mana;
    int attack;
    int health;
    BigDecimal marketPrice;

    @OneToMany(mappedBy = "card")
    List<Listings> listings;

    @OneToMany(mappedBy = "card")
    List<Transaction> transactions;

    @OneToMany(mappedBy = "card")
    List<UserCard> ownedByUsers;

    @OneToMany(mappedBy = "card")
    List<CardEffectBinding> cardEffectBindings;

    public enum cardType {
        SPELL,
        MINION
    }
}
