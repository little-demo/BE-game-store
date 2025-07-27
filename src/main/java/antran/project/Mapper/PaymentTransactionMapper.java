package antran.project.Mapper;

import antran.project.DTO.Response.PaymentTransactionResponse;
import antran.project.Entity.PaymentTransaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentTransactionMapper {
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "status", target = "status")
    PaymentTransactionResponse toPaymentTransactionResponse(PaymentTransaction transaction);
}
