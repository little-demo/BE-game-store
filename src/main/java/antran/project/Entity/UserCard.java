package antran.project.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "user_cards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @ManyToOne
    @JoinColumn(name = "card_id", nullable = false)
    Card card;

    int quantity; //tổng số thẻ của 1 thẻ nguời dùng đang sở hữu
    int deckQuantity; //số lượng thẻ trong bộ bài của người dùng
    int marketQuantity; //số lượng thẻ đang được rao bán trên thị trường, nếu không rao bán thì để 0
}
