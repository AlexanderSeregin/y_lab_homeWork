package website.ylab.learningplatform.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import website.ylab.learningplatform.model.Notification;
import website.ylab.learningplatform.web.dto.NotificationDto;

@Mapper
public interface NotificationMapper {
    NotificationMapper INSTANCE = Mappers.getMapper(NotificationMapper.class);
    
    NotificationDto toDto(Notification notification);

    Notification toEntity(NotificationDto notificationDto);
}
