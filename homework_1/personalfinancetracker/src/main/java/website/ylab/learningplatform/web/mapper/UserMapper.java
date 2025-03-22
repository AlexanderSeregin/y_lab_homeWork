package website.ylab.learningplatform.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import website.ylab.learningplatform.model.User;
import website.ylab.learningplatform.web.dto.UserDto;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    //FIXME @Mapping(target = "admin", source = "admin")
    UserDto toDto(User user);

    //FIXME @Mapping(target = "admin", source = "admin")
    User toEntity(UserDto userDto);
}
