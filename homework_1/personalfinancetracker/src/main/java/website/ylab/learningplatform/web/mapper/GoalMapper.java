package website.ylab.learningplatform.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import website.ylab.learningplatform.model.Goal;
import website.ylab.learningplatform.web.dto.GoalDto;

@Mapper
public interface GoalMapper {
    GoalMapper INSTANCE = Mappers.getMapper(GoalMapper.class);

    GoalDto toDto(Goal goal);

    Goal toEntity(GoalDto goalDto);
}
