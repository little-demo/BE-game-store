package antran.project.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_quests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserQuest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @ManyToOne
    @JoinColumn(name = "quest_id", nullable = false)
    Quest quest;

    int progress = 0;

    boolean isCompleted = false;
    boolean isClaimed = false; // đã nhận thưởng chưa

    LocalDateTime assignedAt = LocalDateTime.now();
    LocalDateTime completedAt;
    LocalDateTime claimedAt;
}
