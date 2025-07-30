package antran.project.Controller;

import antran.project.DTO.ApiResponse;
import antran.project.DTO.Request.CardEffectRequest;
import antran.project.DTO.Response.CardEffectResponse;
import antran.project.Service.CardEffectService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/cardEffects")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CardEffectController {
    CardEffectService cardEffectService;

    @PostMapping
    public ApiResponse<CardEffectResponse> createEffect(@RequestBody CardEffectRequest request) {
        return ApiResponse.<CardEffectResponse>builder()
                .result(cardEffectService.createEffect(request))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<CardEffectResponse> updateEffect(@PathVariable Long id, @RequestBody CardEffectRequest request) {
        return ApiResponse.<CardEffectResponse>builder()
                .result(cardEffectService.updateEffect(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteEffect(@PathVariable Long id) {
        cardEffectService.deleteEffect(id);
        return ApiResponse.<String>builder()
                .result("Effect has been deleted successfully")
                .build();
    }

    @GetMapping
    public ApiResponse<List<CardEffectResponse>> getAllEffects() {
        return ApiResponse.<List<CardEffectResponse>>builder()
                .result(cardEffectService.getAllEffects())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<CardEffectResponse> getEffect(@PathVariable Long id) {
        return ApiResponse.<CardEffectResponse>builder()
                .result(cardEffectService.getEffectById(id))
                .build();
    }
}
