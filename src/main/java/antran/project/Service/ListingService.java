package antran.project.Service;

import antran.project.DTO.Request.ListingRequest;
import antran.project.DTO.Response.ListingResponse;
import antran.project.Entity.*;
import antran.project.Exception.AppException;
import antran.project.Exception.ErrorCode;
import antran.project.Mapper.ListingMapper;
import antran.project.Repository.*;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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
    TransactionRepository transactionRepo;

    NotificationService notificationService;

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

        listingsRepository.save(listing);

        return listingMapper.toListingResponse(listing);
    }


    public List<ListingResponse> getMyListings() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUsername(name)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        List<Listings> listings = listingsRepository.findBySellerAndIsCancelledFalse(user);

        return listings.stream()
                .map(listingMapper::toListingResponse)
                .collect(Collectors.toList());
    }

    public List<ListingResponse> getAllListings() {
        List<Listings> listings = listingsRepository.findByIsCancelledFalseAndQuantityGreaterThan(0);

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

        if (Boolean.TRUE.equals(listing.isCancelled())) {
            throw new AppException(ErrorCode.LISTING_ALREADY_CANCELLED);
        }

        // Trả lại thẻ về cho user
        UserCard userCard = userCardRepository.findByUserAndCard(user, listing.getCard())
                .orElseThrow(() -> new AppException(ErrorCode.USER_CARD_NOT_FOUND));

        userCard.setQuantity(userCard.getQuantity() + listing.getQuantity());
        userCardRepository.save(userCard);

        listing.setCancelled(true);
        listingsRepository.save(listing);
    }

    public void buyCard(Long listingId, int quantity) {
        try {
            Listings listing = listingsRepository.findById(listingId)
                    .orElseThrow(() -> new AppException(ErrorCode.LISTING_NOT_FOUND));

            var context = SecurityContextHolder.getContext();
            String username = context.getAuthentication().getName();

            User buyer = userRepository.findByUsername(username)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
            User seller = listing.getSeller();
            Card card = listing.getCard();

            if (listing.getQuantity() < quantity) {
                throw new AppException(ErrorCode.NOT_ENOUGH_CARD_QUANTITY);
            }

            BigDecimal totalAmount = listing.getSellingPrice().multiply(BigDecimal.valueOf(quantity));
            if (buyer.getBalance() == null || buyer.getBalance().compareTo(totalAmount) < 0) {
                throw new AppException(ErrorCode.NOT_ENOUGH_BALANCE);
            }

            listing.setQuantity(listing.getQuantity() - quantity);

            buyer.setBalance(buyer.getBalance().subtract(totalAmount));
            seller.setBalance(seller.getBalance().add(totalAmount));

            Transaction tx = new Transaction();
            tx.setTransactionCode("TX-" + System.currentTimeMillis());
            tx.setBuyer(buyer);
            tx.setSeller(seller);
            tx.setListing(listing);
            tx.setCard(card);
            tx.setAmount(totalAmount);
            tx.setTransactionDate(LocalDateTime.now());
            tx.setQuantity(quantity);
            transactionRepo.save(tx);

            Optional<UserCard> existing = userCardRepository.findByUserAndCard(buyer, card);
            if (existing.isPresent()) {
                existing.get().setQuantity(existing.get().getQuantity() + quantity);
            } else {
                UserCard newCard = new UserCard();
                newCard.setUser(buyer);
                newCard.setCard(card);
                newCard.setQuantity(quantity);
                userCardRepository.save(newCard);
            }

            listingsRepository.save(listing);
            userRepository.save(buyer);
            userRepository.save(seller);

            // Gửi thông báo cho người bán
            String notifTitle = "Thẻ bài đã được bán!";
            String notifMessage = String.format("Thẻ '%s' của bạn đã được mua bởi '%s' (%d thẻ)",
                    card.getName(), buyer.getUsername(), quantity);

            notificationService.createNotificationForUser(
                    seller,
                    notifTitle,
                    notifMessage,
                    Notification.NotificationType.TRANSACTION
            );

        } catch (Exception e) {
            log.error("Lỗi khi mua thẻ: " + e.getMessage(), e);
            throw e;
        }
    }
}
