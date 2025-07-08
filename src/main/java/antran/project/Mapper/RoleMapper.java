package antran.project.Mapper;

import antran.project.DTO.Request.RoleRequest;
import antran.project.DTO.Response.RoleResponse;
import antran.project.Entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    Role toRole(RoleRequest request);
    RoleResponse toRoleResponse(Role role);
}
