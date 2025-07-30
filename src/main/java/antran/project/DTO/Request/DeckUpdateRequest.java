package antran.project.DTO.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeckUpdateRequest {
    private Long cardId;
    private int quantity; // Số lượng thêm/gỡ
}
