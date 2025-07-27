package antran.project.Repository;

import antran.project.Entity.User;
import antran.project.Entity.UserNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserNotificationRepository extends JpaRepository<UserNotification, Long> {
  List<UserNotification> findAllByUser(User user);
  Optional<UserNotification> findByUserIdAndNotificationId(Long userId, Long notificationId);
}