package antran.project.DTO.Request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CardUpdateDto {
    private Long cardId;
    private int quantity;
}
