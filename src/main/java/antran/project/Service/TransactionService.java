package antran.project.Service;

import antran.project.Entity.User;
import antran.project.Exception.AppException;
import antran.project.Exception.ErrorCode;
import antran.project.Repository.TransactionRepository;
import antran.project.Repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class TransactionService {
    TransactionRepository transactionRepository;
    UserRepository userRepository;

    public List<?> getMyTransactions() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUsername(name)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return transactionRepository.findAll(); // Placeholder for actual implementation
    }
}
