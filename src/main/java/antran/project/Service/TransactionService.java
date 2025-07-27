package antran.project.Service;

import antran.project.DTO.Response.TransactionResponse;
import antran.project.Entity.Transaction;
import antran.project.Entity.User;
import antran.project.Exception.AppException;
import antran.project.Exception.ErrorCode;
import antran.project.Mapper.TransactionMapper;
import antran.project.Repository.TransactionRepository;
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
public class TransactionService {
    TransactionRepository transactionRepository;
    UserRepository userRepository;

    TransactionMapper transactionMapper;

    public List<TransactionResponse> getMyTransactions() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUsername(name)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        List<Transaction> transactions = transactionRepository.findAllByBuyerOrSeller(user, user);
        return transactions.stream()
                .map(transaction -> {
                    TransactionResponse response = transactionMapper.toTransactionResponse(transaction);

                    if (transaction.getBuyer().getId().equals(user.getId())) {
                        response.setTransactionType("BUY");
                    } else if (transaction.getSeller().getId().equals(user.getId())) {
                        response.setTransactionType("SELL");
                    }

                    return response;
                })
                .collect(Collectors.toList());
    }

    public List<TransactionResponse> getAllTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();
        return transactions.stream()
                .map(transactionMapper::toTransactionResponse)
                .collect(Collectors.toList());
    }
}
