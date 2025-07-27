package antran.project.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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

    String title;
    String message;

    @Enumerated(EnumType.STRING)
    NotificationType type;

    @CreationTimestamp
    @Column(updatable = false)
    LocalDateTime createdAt;

    @OneToMany(mappedBy = "notification", cascade = CascadeType.ALL)
    Set<UserNotification> userNotifications = new HashSet<>();

    public enum NotificationType {
        TRANSACTION,
        LISTING,
        SYSTEM,
        PROMOTION,
        EVENT
    }
}