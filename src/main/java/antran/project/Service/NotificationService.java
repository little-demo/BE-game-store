package antran.project.Service;

import antran.project.DTO.Request.NotificationDTO;
import antran.project.DTO.Request.NotificationRequest;
import antran.project.DTO.Request.NotificationUpdateRequest;
import antran.project.DTO.Response.NotificationResponse;
import antran.project.Entity.Notification;
import antran.project.Entity.User;
import antran.project.Entity.UserNotification;
import antran.project.Exception.AppException;
import antran.project.Exception.ErrorCode;
import antran.project.Mapper.NotificationMapper;
import antran.project.Repository.NotificationRepository;
import antran.project.Repository.UserNotificationRepository;
import antran.project.Repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class NotificationService {
    NotificationRepository notificationRepository;
    UserNotificationRepository userNotificationRepository;
    UserRepository userRepository;

    NotificationMapper notificationMapper;

    RealtimeService realtimeService;

    public NotificationResponse createNotification(NotificationRequest request) {
        List<User> allUsers = userRepository.findAll();

        Notification notification = Notification.builder()
                .title(request.getTitle())
                .message(request.getMessage())
                .type(request.getType())  // ← Bất kỳ type nào hợp lệ trong enum
                .build();

        Notification savedNotification = notificationRepository.save(notification);

        List<UserNotification> userNotifications = new ArrayList<>();
        for (User user : allUsers) {
            userNotifications.add(UserNotification.builder()
                    .user(user)
                    .notification(savedNotification)
                    .isRead(false)
                    .build());
        }

        userNotificationRepository.saveAll(userNotifications);

        return NotificationResponse.builder()
                .id(savedNotification.getId())
                .title(savedNotification.getTitle())
                .message(savedNotification.getMessage())
                .type(savedNotification.getType().name())
                .isRead(false)
                .createdAt(savedNotification.getCreatedAt())
                .build();
    }

    public List<NotificationResponse> getAllNotificationsForAdmin() {
        List<Notification.NotificationType> excludedTypes = List.of(
                Notification.NotificationType.TRANSACTION,
                Notification.NotificationType.LISTING
        );

        List<Notification> notifications = notificationRepository.findByTypeNotIn(excludedTypes);

        return notifications.stream()
                .map(notificationMapper::toNotificationResponse)
                .toList();
    }

    public NotificationResponse updateNotification(Long id, NotificationUpdateRequest request) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOTIFICATION_NOT_FOUND));

        notification.setTitle(request.getTitle());
        notification.setMessage(request.getMessage());
        notification.setType(request.getType());

        Notification updated = notificationRepository.save(notification);
        return notificationMapper.toNotificationResponse(updated);
    }

    public List<NotificationResponse> getMyNotifications() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        List<UserNotification> userNotifications = userNotificationRepository.findAllByUser(user);

        return userNotifications.stream()
                .map(notificationMapper::toNotificationResponse)
                .toList();
    }

    public Notification createNotificationForUser(User recipient, String title, String message, Notification.NotificationType type) {
        // Tạo thông báo
        Notification notification = Notification.builder()
                .title(title)
                .message(message)
                .type(type)
                .build();

        notification = notificationRepository.save(notification); // lưu trước để có ID

        // Tạo bản ghi liên kết người dùng và thông báo
        UserNotification userNotification = UserNotification.builder()
                .user(recipient)
                .notification(notification)
                .isRead(false)
                .build();

        userNotificationRepository.save(userNotification);
        // 3. Map DTO
        NotificationDTO dto = NotificationMapper.toDTO(notification, userNotification);

        // 4. Gửi WebSocket
        try {
            realtimeService.sendNotification(recipient.getUsername(), dto);
        } catch (Exception e) {
            log.error("Failed to send realtime notification", e);
        }
        return notification;
    }

    public void markAsRead(Long userNotificationId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        UserNotification userNotification =
                userNotificationRepository.findByUserIdAndNotificationId(user.getId(),userNotificationId)
                .orElseThrow(() -> new AppException(ErrorCode.NOTIFICATION_NOT_FOUND));

        userNotification.setIsRead(true);
        userNotificationRepository.save(userNotification);
    }

    public void deleteNotification(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new AppException(ErrorCode.NOTIFICATION_NOT_FOUND));

        // Xóa notification và tất cả userNotifications liên quan
        notificationRepository.delete(notification);
    }
}
