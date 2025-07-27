package antran.project.DTO.Request;

import antran.project.Entity.Notification;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationRequest {
    String title;
    String message;
    Notification.NotificationType type;
}
