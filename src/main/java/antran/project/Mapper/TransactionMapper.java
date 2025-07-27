package antran.project.Mapper;

import antran.project.DTO.Response.TransactionResponse;
import antran.project.Entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    @Mapping(source = "buyer.username", target = "buyerName")
    @Mapping(source = "seller.username", target = "sellerName")
    @Mapping(source = "card.name", target = "cardName")
    @Mapping(source = "card.overallImageUrl", target = "cardImageUrl")
    TransactionResponse toTransactionResponse(Transaction transaction);
}
