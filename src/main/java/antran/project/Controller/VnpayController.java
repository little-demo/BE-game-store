package antran.project.Controller;

import antran.project.DTO.ApiResponse;
import antran.project.DTO.Request.VnpayRequest;
import antran.project.DTO.Response.PaymentTransactionResponse;
import antran.project.Entity.PaymentTransaction;
import antran.project.Service.VnpayService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/vnpayment")
@AllArgsConstructor
public class VnpayController {

    private final VnpayService vnpayService;

    @PostMapping
    public ResponseEntity<String> createPayment(@RequestBody VnpayRequest paymentRequest) {
        try {
            String paymentUrl = vnpayService.createPayment(paymentRequest);
            return ResponseEntity.ok(paymentUrl);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Đã xảy ra lỗi khi tạo thanh toán!");
        }
    }

    @GetMapping("/return")
    public ResponseEntity<String> returnPayment(HttpServletRequest request) {
        Map<String, String> params = request.getParameterMap().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue()[0]));

        log.info("VNPay return raw params: {}", params);
        return vnpayService.handlePaymentReturn(params);
    }

    @GetMapping("/myPaymentTransactions")
    public ApiResponse<List<PaymentTransactionResponse>> getMyTransactions() {
        return ApiResponse.<List<PaymentTransactionResponse>>builder()
                .message("Lấy lịch sử giao dịch thành công")
                .result(vnpayService.getMyTransactions())
                .build();
    }

    @GetMapping("/allPaymentTransactions")
    public ApiResponse<List<PaymentTransactionResponse>> getAllTransactions() {
        return ApiResponse.<List<PaymentTransactionResponse>>builder()
                .message("Lấy tất cả giao dịch thành công")
                .result(vnpayService.getAllTransactions())
                .build();
    }
}
