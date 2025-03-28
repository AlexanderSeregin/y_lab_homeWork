package website.ylab.learningplatform.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import website.ylab.learningplatform.model.User;
import website.ylab.learningplatform.web.dto.UserDto;

@Mapper(componentModel = "spring")
@Component
public interface UserMapper {
    UserDto toDto(User user);

    @Mapping(target = "isBlocked", ignore = true)
    User toEntity(UserDto userDto);
}
