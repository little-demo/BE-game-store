package antran.project.Mapper;

import antran.project.DTO.Request.UserCreationRequest;
import antran.project.DTO.Request.UserUpdateRequest;
import antran.project.DTO.Response.UserResponse;
import antran.project.Entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
    UserResponse toUserResponse(User user);
}
