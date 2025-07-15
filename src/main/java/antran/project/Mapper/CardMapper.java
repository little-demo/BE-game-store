package antran.project.Mapper;

import antran.project.DTO.Request.CardCreationRequest;
import antran.project.DTO.Response.CardResponse;
import antran.project.Entity.Card;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CardMapper {
    @Mapping(target = "cardType", expression = "java(Card.cardType.valueOf(request.getCardType()))")
    Card toCard(CardCreationRequest request);

    @Mapping(target = "cardType", expression = "java(card.getCardType().name())")
    CardResponse toCardResponse(Card card);
}
