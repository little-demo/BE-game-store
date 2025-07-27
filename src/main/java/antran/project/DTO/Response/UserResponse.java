package antran.project.DTO.Response;

import antran.project.Entity.Role;
import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    Long id;
    String username;
    String email;
    String first_name;
    String last_name;
    LocalDate dob;
    String avatar;
    Boolean enabled;
    BigDecimal balance;
    Set<Role> roles;
}
