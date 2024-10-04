package xyz.notagardener.common.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import xyz.notagardener.common.notification.enums.NotificationType;

@AllArgsConstructor
@Getter
public class Notification {
    private NotificationType type;
}
