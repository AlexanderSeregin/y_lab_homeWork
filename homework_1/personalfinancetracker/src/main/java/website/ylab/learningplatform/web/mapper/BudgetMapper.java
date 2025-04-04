package website.ylab.learningplatform.web.mapper;

import org.mapstruct.Mapper;
import website.ylab.learningplatform.model.Budget;
import website.ylab.learningplatform.web.dto.BudgetDto;

@Mapper
public interface BudgetMapper {

    BudgetDto toDto(Budget budget);

    Budget toEntity(BudgetDto budgetDto);
}
