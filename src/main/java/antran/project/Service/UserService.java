package antran.project.Service;

import antran.project.DTO.Request.UserCreationRequest;
import antran.project.DTO.Request.UserUpdateRequest;
import antran.project.DTO.Response.UserResponse;
import antran.project.Entity.User;
import antran.project.Entity.Role;
import antran.project.Exception.AppException;
import antran.project.Exception.ErrorCode;
import antran.project.Mapper.UserMapper;
import antran.project.Repository.RoleRepository;
import antran.project.Repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) // Using Lombok to reduce boilerplate code
//= private final ...
@Slf4j
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;

    public UserResponse createRequest(UserCreationRequest request) {
        if (userRepository.existsByUsername(request.getUsername()))
            throw new AppException(ErrorCode.USER_EXISTED); //Error is already defined in ErrorCode.java
        User user = userMapper.toUser(request);

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Set default role
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);
        user.setBalance(BigDecimal.ZERO);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    //get my info from security context after login
    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUsername(name)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        log.info("Balance of user {}: {}", user.getUsername(), user.getBalance());
        return userMapper.toUserResponse(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUsers() {
        log.info("Fetching all users");
        return userRepository.findAll()
                .stream().map(userMapper::toUserResponse).toList();
    }

    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse getUser(Long id) {
        log.info("Fetching user with ID: {}", id);
        return userMapper.toUserResponse(userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found")));
    }

    public UserResponse updateUser(Long id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userMapper.updateUser(user, request);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public List<UserResponse> searchBySingleCriteria(String username, String email) {
        List<User> users;

        if (username != null) {
            users = userRepository.findByUsernameContainingIgnoreCase(username);
        } else if (email != null) {
            users = userRepository.findByEmailContainingIgnoreCase(email);
        }else {
            throw new IllegalArgumentException("No filter provided.");
        }

        return users.stream()
                .map(userMapper::toUserResponse)
                .toList();
    }

    public UserResponse changeUserStatus(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        user.setEnabled(user.getEnabled() == null || !user.getEnabled()); // Nếu null → mặc định là true
        log.info("Toggled user ID {} to enabled = {}", id, user.getEnabled());

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public UserResponse changePassword(Long id, String oldPassword, String newPassword) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Kiểm tra mật khẩu cũ
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new AppException(ErrorCode.OLD_PASSWORD_INCORRECT);
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        return userMapper.toUserResponse(userRepository.save(user));
    }
}
