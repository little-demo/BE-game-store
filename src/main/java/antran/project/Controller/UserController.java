package antran.project.Controller;

import antran.project.DTO.ApiResponse;
import antran.project.DTO.Request.UserCreationRequest;
import antran.project.DTO.Request.UserUpdateRequest;
import antran.project.DTO.Response.UserResponse;
import antran.project.Entity.User;
import antran.project.Service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    @PostMapping
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        ApiResponse<UserResponse> userApiResponse = new ApiResponse<>();
        userApiResponse.setResult(userService.createRequest(request));
        return userApiResponse;
    }

    @GetMapping
    ApiResponse<List<UserResponse>> getUsers() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Username: {}", authentication.getName());
        authentication.getAuthorities().forEach(authority ->
            log.info("Authority: {}", authority.getAuthority())
        );
        List<UserResponse> allUsers = userService.getUsers();

        List<UserResponse> filteredUsers = allUsers.stream()
                .filter(user -> user.getRoles().stream()
                        .noneMatch(role -> role.getName().equalsIgnoreCase("ADMIN")))
                .toList();

        return ApiResponse.<List<UserResponse>>builder()
                .result(filteredUsers)
                .build();
    }

    @GetMapping("/{userId}")
    ApiResponse<UserResponse> getUser(@PathVariable("userId") String userId) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUser(Long.valueOf(userId)))
                .build();
    }

    @GetMapping("/myInfo")
    ApiResponse<UserResponse> getMyInfo() {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInfo())
                .build();
    }

    @DeleteMapping("/{userId}")
    ApiResponse<String> deleteUser(@PathVariable String userId) {
        userService.deleteUser(Long.valueOf(userId));
        return ApiResponse.<String>builder().result("User has been deleted").build();
    }

    @PutMapping("/{userId}")
    ApiResponse<UserResponse> updateUser(@PathVariable String userId, @RequestBody UserUpdateRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateUser(Long.valueOf(userId), request))
                .build();
    }

    @GetMapping("/search")
    ApiResponse<List<UserResponse>> searchUsers(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String role
    ) {
        // Kiểm tra chỉ có một tiêu chí được truyền vào
        int filterCount = 0;
        if (username != null) filterCount++;
        if (email != null) filterCount++;
        if (role != null) filterCount++;

        if (filterCount != 1) {
            throw new IllegalArgumentException("Please provide exactly one filter: username, email, or role.");
        }

        List<UserResponse> users = userService.searchBySingleCriteria(username, email, role);

        // Lọc bỏ user có role ADMIN
        List<UserResponse> filteredUsers = users.stream()
                .filter(user -> user.getRoles().stream()
                        .noneMatch(r -> r.getName().equalsIgnoreCase("ADMIN")))
                .toList();

        return ApiResponse.<List<UserResponse>>builder()
                .result(filteredUsers)
                .build();
    }

}
