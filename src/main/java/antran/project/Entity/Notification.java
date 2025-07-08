package antran.project.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    String title;
    String message;
    @Enumerated(EnumType.STRING)
    NotificationType type;

    boolean isRead = false;

    public enum NotificationType {
        TRANSACTION,
        LISTING,
        SYSTEM,
        PROMOTION
    }
}
