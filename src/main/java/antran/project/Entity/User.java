package antran.project.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String username;
    String email;
    String password;
    String first_name;
    String last_name;
    LocalDate dob;
    BigDecimal balance = BigDecimal.ZERO;
    String avatar;
    Boolean enabled = true;

    @ManyToMany
    Set<Role> roles;

    @OneToMany(mappedBy = "seller")
    List<Listings> listings;

    @OneToMany(mappedBy = "buyer")
    List<Transaction> purchases;

    @OneToMany(mappedBy = "seller")
    List<Transaction> sales;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    Set<UserNotification> userNotifications = new HashSet<>();

    @OneToMany(mappedBy = "user")
    List<UserCard> ownedCards;

    @OneToMany(mappedBy = "user")
    List<Reward> rewards;

    @OneToMany(mappedBy = "user")
    List<UserQuest> quests;
}
