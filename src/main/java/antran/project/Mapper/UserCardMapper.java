package antran.project.Mapper;

import antran.project.DTO.Response.UserCardResponse;
import antran.project.Entity.UserCard;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface UserCardMapper {
    @Mapping(source = "id", target = "userCardId")
    @Mapping(source = "card.id", target = "cardId")
    @Mapping(source = "card.name", target = "cardName")
    @Mapping(source = "card.cardType", target = "cardType")
    @Mapping(source = "card.description", target = "description")
    @Mapping(source = "card.imageUrl", target = "imageUrl")
    @Mapping(source = "card.overallImageUrl", target = "overallImageUrl")
    @Mapping(source = "card.mana", target = "mana")
    @Mapping(source = "card.attack", target = "attack")
    @Mapping(source = "card.health", target = "health")
    @Mapping(source = "card.marketPrice", target = "marketPrice")
    UserCardResponse toUserCardResponse(UserCard userCard);
}
