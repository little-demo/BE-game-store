package antran.project.Controller;

import antran.project.DTO.ApiResponse;
import antran.project.DTO.Request.BuyCardRequest;
import antran.project.DTO.Request.ListingRequest;
import antran.project.DTO.Response.ListingResponse;
import antran.project.Entity.Listings;
import antran.project.Repository.ListingsRepository;
import antran.project.Service.ListingService;
import antran.project.Service.NotificationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/listings")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ListingController {
    ListingsRepository listingsRepository;
    ListingService listingService;
    NotificationService notificationService;

    @PostMapping
    ApiResponse<ListingResponse> createListing(@RequestBody ListingRequest request) {
        ListingResponse response = listingService.createListing(request);

        return ApiResponse.<ListingResponse>builder()
                .result(response)
                .message("Listing created successfully")
                .build();
    }

    @GetMapping("/myListing")
    ApiResponse<List<ListingResponse>> getMyListings() {
        List<ListingResponse> responses = listingService.getMyListings();

        return ApiResponse.<List<ListingResponse>>builder()
                .result(responses)
                .message("My listings retrieved successfully")
                .build();
    }

    @GetMapping
    ApiResponse<List<ListingResponse>> getAllListings() {
        List<ListingResponse> responses = listingService.getAllListings(); // Assume this method exists in ListingService
        return ApiResponse.<List<ListingResponse>>builder()
                .result(responses)
                .message("All listings retrieved successfully")
                .build();
    }

    @DeleteMapping("/{listingId}/cancel")
    public ApiResponse<String> cancelListing(@PathVariable Long listingId) {
        listingService.cancelListing(listingId);
        return ApiResponse.<String>builder()
                .message("Đã thu hồi thẻ bài khỏi danh sách đăng bán")
                .result("Success")
                .build();
    }

    @PostMapping("/{listingId}/buy")
    public ApiResponse<String> buyCard(@PathVariable Long listingId, @RequestBody BuyCardRequest request) {
        log.info("Buying card. ListingId: {}, Quantity: {}", listingId, request.getQuantity());

        listingService.buyCard(listingId, request.getQuantity());

        return ApiResponse.<String>builder()
                .result("Success")
                .message("Mua thẻ bài thành công")
                .build();
    }
}
