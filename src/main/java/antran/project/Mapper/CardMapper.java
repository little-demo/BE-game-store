package antran.project.Mapper;

import antran.project.DTO.Request.CardCreationRequest;
import antran.project.DTO.Response.CardResponse;
import antran.project.Entity.Card;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CardMapper {
    @Mapping(target = "cardType", expression = "java(Card.cardType.valueOf(request.getCardType()))")
    Card toCard(CardCreationRequest request);

    @Mapping(target = "cardType", expression = "java(card.getCardType().name())")
    @Mapping(target = "effects", expression = "java(mapEffects(card))") // Thêm dòng này để map thủ công hiệu ứng
    CardResponse toCardResponse(Card card);

    // Hàm hỗ trợ để map danh sách hiệu ứng và timing
    default List<CardResponse.EffectWithTiming> mapEffects(Card card) {
        if (card.getCardEffectBindings() == null) return new ArrayList<>();

        return card.getCardEffectBindings().stream()
                .map(binding -> CardResponse.EffectWithTiming.builder()
                        .effectId(binding.getEffect().getId())
                        .effectName(binding.getEffect().getName())
                        .timing(binding.getTiming().name())
                        .build())
                .collect(Collectors.toList());
    }
}
