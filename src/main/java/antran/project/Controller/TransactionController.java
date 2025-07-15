package antran.project.Controller;

import antran.project.DTO.ApiResponse;
import antran.project.Service.TransactionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TransactionController {
    TransactionService transactionService;

    @GetMapping
    public ApiResponse<List<?>> getMyTransactions() {
        List<?> transactions = transactionService.getMyTransactions();
        return ApiResponse.<List<?>>builder()
                .result(transactions)
                .message("Fetched all transactions successfully")
                .build();
    }
}
