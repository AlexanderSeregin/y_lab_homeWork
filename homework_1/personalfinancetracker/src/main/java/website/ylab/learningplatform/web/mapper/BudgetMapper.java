package website.ylab.learningplatform.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import website.ylab.learningplatform.model.Budget;
import website.ylab.learningplatform.web.dto.BudgetDto;

@Mapper
public interface BudgetMapper {
    BudgetMapper INSTANCE = Mappers.getMapper(BudgetMapper.class);
    
    BudgetDto toDto(Budget budget);
    
    Budget toEntity(BudgetDto budgetDto);
}
