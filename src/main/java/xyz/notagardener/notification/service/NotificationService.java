package xyz.notagardener.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.notagardener.authentication.repository.ActiveGardenerRepository;
import xyz.notagardener.common.model.Notifiable;
import xyz.notagardener.notification.dto.NotificationResponse;
import xyz.notagardener.notification.model.Notification;
import xyz.notagardener.notification.repository.NotificationRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final ActiveGardenerRepository activeGardenerRepository;
    private final SimpMessagingTemplate messagingTemplate; // 웹소켓 메시지 전송
    private static final String DESTINATION = "/queue/notifications/";

    @Transactional(readOnly = true)
    public List<NotificationResponse> getUnreadAll(Long gardenerId) {
        return notificationRepository.findByGardener_GardenerId(gardenerId).stream().map(this::toDto).toList();
    }

    private NotificationResponse toDto(Notification notification) {
        Notifiable notifiable = switch (notification.getType()) {
            case COMMENT -> notification.getComment();
            case FOLLOW -> notification.getFollow();
            case LIKE -> notification.getLike();
            case POST -> notification.getPost();
        };

        return new NotificationResponse(notifiable);
    }

    public void send(Notifiable notifiable, Long targetId) {
        Notification notification = notificationRepository.save(new Notification(notifiable));

        // 로그인한 사용자에게만 메시지 전송
        if (isUserLoggedIn(targetId)) {
            messagingTemplate.convertAndSend(DESTINATION + targetId, toDto(notification));
        }
    }

    // 여러 명한테 보내기
    public void send(Notifiable notifiable, List<Long> targetIds) {
        targetIds.stream()
                .filter(this::isUserLoggedIn) // 로그인한 사용자만 필터링
                .map(targetId -> {
                    Notification notification = notificationRepository.save(new Notification(notifiable));
                    return toDto(notification);
                })
                .forEach(notificationDto ->
                        messagingTemplate.convertAndSend(DESTINATION + notificationDto.getTargetId(), notificationDto)
                );
    }

    // 사용자 로그인 상태 확인
    private boolean isUserLoggedIn(Long gardenerId) {
        return activeGardenerRepository.findById(gardenerId).isPresent(); // 로그인한 사용자 확인
    }
}

