package antran.project.DTO.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class NotificationDTO {
    private Long id;
    private String title;
    private String message;
    private String type;
    private LocalDateTime createdAt;

    private boolean isRead;
    private Long userId;
}
