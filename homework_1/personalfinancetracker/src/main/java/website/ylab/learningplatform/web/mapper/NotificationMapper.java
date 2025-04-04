package website.ylab.learningplatform.web.mapper;

import org.mapstruct.Mapper;
import website.ylab.learningplatform.model.Notification;
import website.ylab.learningplatform.web.dto.NotificationDto;

@Mapper
public interface NotificationMapper {

    NotificationDto toDto(Notification notification);

    Notification toEntity(NotificationDto notificationDto);
}
