package antran.project.Controller;

import antran.project.DTO.ApiResponse;
import antran.project.DTO.Request.CardCreationRequest;
import antran.project.DTO.Response.CardResponse;
import antran.project.DTO.Response.UserCardResponse;
import antran.project.Service.CardService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/cards")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CardController {
    CardService cardService;

    @PostMapping
    ApiResponse<CardResponse> createCard(@RequestBody CardCreationRequest request) {
        return ApiResponse.<CardResponse>builder()
                .result(cardService.createCard(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<CardResponse>> getAllCards() {
        return ApiResponse.<List<CardResponse>>builder()
                .result(cardService.getAllCards())
                .build();
    }

    @GetMapping("/{cardId}")
    ApiResponse<CardResponse> getCardById(@PathVariable Long cardId) {
        return ApiResponse.<CardResponse>builder()
                .result(cardService.getCardById(cardId))
                .build();
    }

    @PutMapping("/{cardId}")
    ApiResponse<CardResponse> updateCard(
            @PathVariable Long cardId,
            @RequestBody CardCreationRequest request
    ) {
        return ApiResponse.<CardResponse>builder()
                .result(cardService.updateCard(cardId, request))
                .build();
    }

    @DeleteMapping("/{cardId}")
    ApiResponse<String> deleteCard(@PathVariable Long cardId) {
        cardService.deleteCard(cardId);
        return ApiResponse.<String>builder()
                .result("Card has been deleted")
                .build();
    }

    @GetMapping("/myCards")
    ApiResponse<List<UserCardResponse>> getMyCards() {
        return ApiResponse.<List<UserCardResponse>>builder()
                .result(cardService.getUserCards())
                .build();
    }
}
