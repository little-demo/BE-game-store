package antran.project.Service;

import antran.project.DTO.Request.ListingRequest;
import antran.project.DTO.Response.ListingResponse;
import antran.project.Entity.Card;
import antran.project.Entity.Listings;
import antran.project.Entity.User;
import antran.project.Entity.UserCard;
import antran.project.Exception.AppException;
import antran.project.Exception.ErrorCode;
import antran.project.Mapper.ListingMapper;
import antran.project.Repository.CardRepository;
import antran.project.Repository.ListingsRepository;
import antran.project.Repository.UserCardRepository;
import antran.project.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ListingService {
    ListingsRepository listingsRepository;
    ListingMapper listingMapper;

    UserRepository userRepository;
    CardRepository cardRepository;

    UserCardRepository userCardRepository;

    public ListingResponse createListing(ListingRequest request) {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUsername(name)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Card card = cardRepository.findById(request.getCardId())
                .orElseThrow(() -> new RuntimeException("Card not found"));

        // Tìm và cập nhật UserCard
        UserCard userCard = userCardRepository.findByUserAndCard(user, card)
                .orElseThrow(() -> new RuntimeException("Not enough cards"));

        if (userCard.getQuantity() < request.getQuantity()) {
            throw new RuntimeException("Not enough cards to create listing");
        }

        userCard.setQuantity(userCard.getQuantity() - request.getQuantity());
        userCardRepository.save(userCard);

        // Tạo listing
        Listings listing = listingMapper.toListing(request);
        listing.setCard(card);
        listing.setSeller(user);
        listing.setPostedAt(LocalDateTime.now());
        listing.setSoldAt(null);

        listingsRepository.save(listing);

        return listingMapper.toListingResponse(listing);
    }


    public List<ListingResponse> getMyListings() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUsername(name)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        List<Listings> listings = listingsRepository.findBySeller(user);

        return listings.stream()
                .map(listingMapper::toListingResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void cancelListing(Long listingId) {
        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Listings listing = listingsRepository.findById(listingId)
                .orElseThrow(() -> new AppException(ErrorCode.LISTING_NOT_FOUND));

        // Chỉ được hủy nếu chưa ai mua
        if (listing.getSoldAt() != null || !listing.getSeller().getId().equals(user.getId())) {
            throw new AppException(ErrorCode.CANNOT_CANCEL_LISTING);
        }

        // Trả lại thẻ về cho user
        UserCard userCard = userCardRepository.findByUserAndCard(user, listing.getCard())
                .orElseThrow(() -> new AppException(ErrorCode.USER_CARD_NOT_FOUND));

        userCard.setQuantity(userCard.getQuantity() + listing.getQuantity());
        userCardRepository.save(userCard);

        listingsRepository.delete(listing);
    }
}
