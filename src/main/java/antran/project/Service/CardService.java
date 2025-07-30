package antran.project.Service;

import antran.project.DTO.Request.CardCreationRequest;
import antran.project.DTO.Response.CardResponse;
import antran.project.DTO.Response.UserCardResponse;
import antran.project.Entity.*;
import antran.project.Exception.AppException;
import antran.project.Exception.ErrorCode;
import antran.project.Mapper.CardMapper;
import antran.project.Mapper.UserCardMapper;
import antran.project.Repository.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CardService {
    private final UserRepository userRepository;
    CardRepository cardRepository;
    CardMapper cardMapper;

    UserCardRepository userCardRepository;
    UserCardMapper userCardMapper;
    CardEffectRepository cardEffectRepository;
    CardEffectBindingRepository cardEffectBindingRepository;

    public List<CardResponse> getAllCards() {
        return cardRepository.findAll()
                .stream()
                .map(cardMapper::toCardResponse)
                .collect(Collectors.toList());
    }

    public CardResponse getCardById(Long id) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thẻ với ID: " + id));
        return cardMapper.toCardResponse(card);
    }

    public CardResponse createCard(CardCreationRequest request) {
        Card card = cardMapper.toCard(request);
        card = cardRepository.save(card);

        if (request.getEffects() != null) {
            for (CardCreationRequest.EffectAssignment assignment : request.getEffects()) {
                CardEffect effect = cardEffectRepository.findById(assignment.getEffectId())
                        .orElseThrow(() -> new RuntimeException("Effect không tồn tại: " + assignment.getEffectId()));
                log.info("hieu ung: " + effect.getName());
                CardEffectBinding binding = CardEffectBinding.builder()
                        .card(card)
                        .effect(effect)
                        .timing(assignment.getTiming())
                        .build();

                cardEffectBindingRepository.save(binding);
            }
        }
        return cardMapper.toCardResponse(cardRepository.save(card));
    }

    public CardResponse updateCard(Long id, CardCreationRequest request) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thẻ với ID: " + id));

        // Cập nhật các trường (MapStruct không hỗ trợ update trực tiếp)
        card.setName(request.getName());
        card.setDescription(request.getDescription());
        card.setCardType(Card.cardType.valueOf(request.getCardType()));
        card.setImageUrl(request.getImageUrl());
        card.setOverallImageUrl(request.getOverallImageUrl());
        card.setMana(request.getMana());
        card.setAttack(request.getAttack());
        card.setHealth(request.getHealth());
        card.setMarketPrice(request.getMarketPrice());

        return cardMapper.toCardResponse(cardRepository.save(card));
    }

    public void deleteCard(Long id) {
        if (!cardRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy thẻ để xóa với ID: " + id);
        }
        cardRepository.deleteById(id);
    }

    public List<UserCardResponse> getUserCards() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUsername(name)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return userCardRepository.findByUserId(user.getId())
                .stream()
                .map(userCardMapper::toUserCardResponse)
                .collect(Collectors.toList());
    }

    public void addCardToDeck(Long cardId, int quantity) {
        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found"));

        UserCard userCard = userCardRepository.findByUserAndCard(user, card)
                .orElseThrow(() -> new RuntimeException("User does not own this card"));

        int currentDeckQuantity = userCard.getDeckQuantity();
        int newDeckQuantity = currentDeckQuantity + quantity;

        // Tổng số thẻ trong deck hiện tại
        List<UserCard> deckCards = userCardRepository.findByUserIdAndDeckQuantityGreaterThan(user.getId(), 0);
        int totalCardsInDeck = deckCards.stream()
                .mapToInt(UserCard::getDeckQuantity)
                .sum();

        if (totalCardsInDeck + quantity > 30) {
            throw new RuntimeException("Tổng số thẻ trong deck không được vượt quá 30");
        }

        if (newDeckQuantity > 3) {
            throw new RuntimeException("Mỗi thẻ chỉ có thể có tối đa 3 bản trong deck");
        }

        if (userCard.getQuantity() < newDeckQuantity) {
            throw new RuntimeException("Bạn không sở hữu đủ thẻ này để thêm vào deck");
        }

        userCard.setDeckQuantity(newDeckQuantity);
        userCardRepository.save(userCard);
    }

    public void removeCardFromDeck(Long cardId) {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUsername(name)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        UserCard userCard = userCardRepository.findByUserIdAndCardId(user.getId(), cardId)
                .orElseThrow(() -> new RuntimeException("Bạn chưa sở hữu thẻ này"));

        if (userCard.getDeckQuantity() < 1) {
            throw new RuntimeException("Không còn thẻ nào trong deck để gỡ");
        }

        userCard.setDeckQuantity(userCard.getDeckQuantity() - 1);
        userCardRepository.save(userCard);
    }

    public List<UserCardResponse> getDeckCards() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUsername(name)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return userCardRepository.findByUserIdAndDeckQuantityGreaterThan(user.getId(), 0)
                .stream()
                .map(userCardMapper::toUserCardResponse)
                .collect(Collectors.toList());
    }
}
