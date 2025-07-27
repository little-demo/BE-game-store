package antran.project.DTO.Request;

import antran.project.Validator.DobConstrain;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    @Size(min = 3, message = "USERNAME_INVALID") //Error is already defined in ErrorCode.java
    String username;
    @Email(message = "Email is not in correct format")
    String email;
    @Size(min = 8, message = "PASSWORD_INVALID")
    String password;
    String first_name;
    String last_name;

    @DobConstrain(min = 16, message = "INVALID_DOB")
    LocalDate dob;
    String avatar; // Optional, can be null

    Boolean enabled = true;
}
