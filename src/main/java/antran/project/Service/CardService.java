package antran.project.Service;

import antran.project.DTO.Request.CardCreationRequest;
import antran.project.DTO.Response.CardResponse;
import antran.project.DTO.Response.UserCardResponse;
import antran.project.Entity.Card;
import antran.project.Entity.User;
import antran.project.Exception.AppException;
import antran.project.Exception.ErrorCode;
import antran.project.Mapper.CardMapper;
import antran.project.Mapper.UserCardMapper;
import antran.project.Repository.CardRepository;
import antran.project.Repository.UserCardRepository;
import antran.project.Repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
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
}
