package antran.project.Repository;

import antran.project.Entity.Notification;
import antran.project.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByType(Notification.NotificationType type);
}