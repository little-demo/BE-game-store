package antran.project.Service;

import antran.project.DTO.Request.CardUpdateDto;
import antran.project.DTO.Request.NotificationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class RealtimeService {
    private final SimpMessagingTemplate messagingTemplate;

    public void sendBalanceUpdate(String username, BigDecimal newBalance) {
        messagingTemplate.convertAndSendToUser(username, "/topic/balance", newBalance);
    }

    public void sendCardQuantityUpdate(String username, Long cardId, int quantity) {
        messagingTemplate.convertAndSendToUser(username, "/topic/cards", new CardUpdateDto(cardId, quantity));
    }

    public void sendNotification(String username, NotificationDTO notification) {
        System.out.println("Sending notification to user: " + username);
        messagingTemplate.convertAndSendToUser(username, "/topic/notifications", notification);
    }
}
