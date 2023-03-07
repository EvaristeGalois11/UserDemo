package it.nave.userdemo.mapper;

import it.nave.userdemo.dto.UserDto;
import it.nave.userdemo.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);

    User toEntity(UserDto dto);
}
