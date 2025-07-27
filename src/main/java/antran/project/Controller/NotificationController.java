package antran.project.Controller;

import antran.project.DTO.ApiResponse;
import antran.project.DTO.Request.NotificationRequest;
import antran.project.DTO.Request.NotificationUpdateRequest;
import antran.project.DTO.Response.NotificationResponse;
import antran.project.Service.NotificationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationController {
    NotificationService notificationService;

    @GetMapping("/myNotifications")
    public ApiResponse<List<NotificationResponse>> getMyNotifications() {
        return ApiResponse.<List<NotificationResponse>>builder()
                .result(notificationService.getMyNotifications())
                .build();
    }

    @PutMapping("/{id}/markAsRead")
    public ApiResponse<Void> markAsRead(@PathVariable("id") String id) {
        log.info("đã đọc thông báo id: " + id);

        notificationService.markAsRead(Long.valueOf(id));
        return ApiResponse.<Void>builder()
                .message("Marked as read successfully")
                .build();
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<NotificationResponse> createNotification(@RequestBody NotificationRequest request) {
        NotificationResponse response = notificationService.createNotification(request);
        return ApiResponse.<NotificationResponse>builder()
                .result(response)
                .message("Notification created successfully")
                .build();
    }

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<NotificationResponse>> getAllNotificationsForAdmin() {
        List<NotificationResponse> notifications = notificationService.getAllNotificationsForAdmin();
        return ApiResponse.<List<NotificationResponse>>builder()
                .result(notifications)
                .message("Fetched all system notifications successfully")
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<NotificationResponse> updateNotification(
            @PathVariable String id,
            @RequestBody NotificationUpdateRequest request
    ) {
        return ApiResponse.<NotificationResponse>builder()
                .result(notificationService.updateNotification(Long.valueOf(id), request))
                .message("Notification updated successfully")
                .build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // chỉ admin mới có quyền xóa thông báo hệ thống
    public ApiResponse<Void> deleteNotification(@PathVariable String id) {
        notificationService.deleteNotification(Long.valueOf(id));
        return ApiResponse.<Void>builder()
                .message("Notification deleted successfully")
                .build();
    }
}
