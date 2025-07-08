package antran.project.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "rewards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Reward {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @Enumerated(EnumType.STRING)
    RewardType type; // COIN, XP, CARD...

    int value;

    @Enumerated(EnumType.STRING)
    RewardSource source; // GAME, QUEST, LOGIN, EVENT...

    String sourceDetail;

    LocalDateTime createdAt = LocalDateTime.now();

    public enum RewardType {
        COIN, CARD
    }

    public enum RewardSource {
        GAME, QUEST, LOGIN, EVENT, SYSTEM
    }
}
