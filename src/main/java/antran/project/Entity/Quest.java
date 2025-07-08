package antran.project.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "quests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Quest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name; // Ví dụ: "Win 3 Games"
    String description;

    @Enumerated(EnumType.STRING)
    GoalType goalType;

    int targetValue; // Ví dụ: 3 (cần 3 trận thắng)

    @Enumerated(EnumType.STRING)
    RewardType rewardType;

    int rewardValue;

    @Enumerated(EnumType.STRING)
    QuestType questType = QuestType.DAILY;

    public enum GoalType {
        WIN_GAME,
        PLAY_CARD,
        USE_MANA,
        DEFEAT_AI,
        DRAW_CARD,
        COLLECT_GOLD
    }

    public enum RewardType {
        COIN,
        CARD,
        XP
    }

    public enum QuestType {
        DAILY,
        WEEKLY,
        EVENT
    }
}
