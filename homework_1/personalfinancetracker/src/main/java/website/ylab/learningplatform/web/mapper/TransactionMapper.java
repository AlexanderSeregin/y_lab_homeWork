package website.ylab.learningplatform.web.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import website.ylab.learningplatform.model.Transaction;
import website.ylab.learningplatform.web.dto.TransactionDto;

@Mapper(componentModel = "spring")
@Component
public interface TransactionMapper {
    TransactionDto toDto(Transaction transaction);

    Transaction toEntity(TransactionDto transactionDto);
}
