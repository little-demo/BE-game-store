package antran.project.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE) //All fields are private
@JsonInclude(JsonInclude.Include.NON_NULL) //Any null element won't displayed
public class ApiResponse <T> {
    @Builder.Default
    int code = 1000; //default is success
    String message;
    T result;
}
