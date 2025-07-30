package antran.project.Mapper;

import antran.project.DTO.Request.CardCreationRequest;
import antran.project.DTO.Request.CardEffectRequest;
import antran.project.DTO.Response.CardEffectResponse;
import antran.project.Entity.CardEffect;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CardEffectMapper {
    CardEffect toCardEffect(CardEffectRequest request);

    CardEffectResponse toCardEffectResponse(CardEffect cardEffect);
}
