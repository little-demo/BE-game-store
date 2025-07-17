package antran.project.Mapper;

import antran.project.DTO.Request.ListingRequest;
import antran.project.DTO.Response.ListingResponse;
import antran.project.Entity.Listings;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ListingMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "card", ignore = true)
    @Mapping(target = "seller", ignore = true)
    @Mapping(target = "postedAt", ignore = true)
    @Mapping(target = "transactions", ignore = true)
    Listings toListing(ListingRequest request);

    @Mapping(target = "sellerName", source = "seller.username")
    @Mapping(target = "cardId", source = "card.id")
    @Mapping(target = "cardName", source = "card.name")
    @Mapping(target = "cardImageUrl", source = "card.overallImageUrl")
    @Mapping(target = "cardType", source = "card.cardType")
    @Mapping(target = "postedAt", source = "postedAt")
    ListingResponse toListingResponse(Listings listing);
}
