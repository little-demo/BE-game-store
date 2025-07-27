package antran.project.Mapper;

import antran.project.DTO.Request.NotificationRequest;
import antran.project.DTO.Response.NotificationResponse;
import antran.project.Entity.Notification;
import antran.project.Entity.UserNotification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    Notification toNotification(NotificationRequest request);

    @Mapping(source = "notification.id", target = "id")
    @Mapping(source = "notification.title", target = "title")
    @Mapping(source = "notification.message", target = "message")
    @Mapping(source = "notification.type", target = "type")
    @Mapping(source = "notification.createdAt", target = "createdAt")
    @Mapping(source = "isRead", target = "isRead")
    NotificationResponse toNotificationResponse(UserNotification userNotification);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "message", target = "message")
    @Mapping(source = "type", target = "type")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(target = "isRead", ignore = true) // Không có thông tin đọc cho admin
    NotificationResponse toNotificationResponse(Notification notification);
}
