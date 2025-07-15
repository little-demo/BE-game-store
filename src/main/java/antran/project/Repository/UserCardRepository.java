package antran.project.Repository;

import antran.project.Entity.Card;
import antran.project.Entity.User;
import antran.project.Entity.UserCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserCardRepository extends JpaRepository<UserCard, Long> {
    List<UserCard> findByUserId(Long userId);
    Optional<UserCard> findByUserAndCard(User user, Card card);
}