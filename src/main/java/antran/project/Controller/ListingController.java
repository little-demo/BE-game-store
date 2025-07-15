package antran.project.Controller;

import antran.project.DTO.ApiResponse;
import antran.project.DTO.Request.ListingRequest;
import antran.project.DTO.Response.ListingResponse;
import antran.project.Service.ListingService;
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
    ListingService listingService;

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

    @DeleteMapping("/{listingId}/cancel")
    public ApiResponse<String> cancelListing(@PathVariable Long listingId) {
        listingService.cancelListing(listingId);
        return ApiResponse.<String>builder()
                .message("Đã thu hồi thẻ bài khỏi danh sách đăng bán")
                .result("Success")
                .build();
    }
}
