package antran.project.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "listings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Listings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    User seller;

    @ManyToOne
    @JoinColumn(name = "card_id", nullable = false)
    Card card;

    BigDecimal sellingPrice;
    LocalDateTime postedAt;
    LocalDateTime soldAt;
    int quantity;

    @OneToMany(mappedBy = "listing")
    List<Transaction> transactions;
}
