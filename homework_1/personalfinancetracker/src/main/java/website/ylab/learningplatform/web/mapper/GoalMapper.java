package website.ylab.learningplatform.web.mapper;

import org.mapstruct.Mapper;
import website.ylab.learningplatform.model.Goal;
import website.ylab.learningplatform.web.dto.GoalDto;

@Mapper
public interface GoalMapper {

    GoalDto toDto(Goal goal);

    Goal toEntity(GoalDto goalDto);
}
