package xyz.notagardener.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.notagardener.authentication.repository.ActiveGardenerRepository;
import xyz.notagardener.comment.model.Comment;
import xyz.notagardener.common.model.Notifiable;
import xyz.notagardener.follow.model.Follow;
import xyz.notagardener.gardener.model.Gardener;
import xyz.notagardener.like.entity.Like;
import xyz.notagardener.notification.dto.NotificationResponse;
import xyz.notagardener.notification.enums.NotificationType;
import xyz.notagardener.notification.model.Notification;
import xyz.notagardener.notification.repository.NotificationRepository;
import xyz.notagardener.post.model.Post;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final ActiveGardenerRepository activeGardenerRepository;
    private final SimpMessagingTemplate messagingTemplate; // 웹소켓 메시지 전송
    private static final String DESTINATION = "/queue/notifications/";

    /**
     * 읽지 않은 알림 리스트
     * @param gardenerId 유저 PK
     * @return 읽지 않은 알림 리스트
     */
    @Transactional(readOnly = true)
    public List<NotificationResponse> getUnreadAll(Long gardenerId) {
        return notificationRepository.findByGardener_GardenerId(gardenerId).stream()
                .map(NotificationResponse::new)
                .toList();
    }

    /**
     * 단일 사용자 알림 저장, 전송
     * @param notifiable 알림 인터페이스
     * @param targetId 알림 받을 유저 ID
     */
    public void send(Notifiable notifiable, Long targetId) {
        Notification notification = save(notifiable, notifiable.getGardener());

        // 로그인한 사용자에게만 메시지 전송
        if (isUserLoggedIn(targetId)) {
            send(notification);
        }
    }

    /**
     * 여러 사용자 알림 저장, 전송
     * @param notifiable 알림 인터페이스
     * @param gardeners 알림 받을 유저 리스트
     */
    public void send(Notifiable notifiable, List<Gardener> gardeners) {
        gardeners.stream()
                .map(gardener -> save(notifiable, gardener))
                .filter((notification -> isUserLoggedIn(notification.getGardener().getGardenerId())))
                .forEach(this::send);
    }

    /**
     * 알림 전송
     * @param notification 알림 엔티티
     */
    private void send(Notification notification) {
        NotificationResponse payload = new NotificationResponse(notification);
        messagingTemplate.convertAndSend(DESTINATION + payload.getTargetId(), payload);
    }

    /**
     * 사용자 로그인 상태 확인
     *
     * @param gardenerId 확인할 가드너 ID
     * @return 로그인 상태 여부
     */
    private boolean isUserLoggedIn(Long gardenerId) {
        return activeGardenerRepository.findById(gardenerId).isPresent(); // 로그인한 사용자 확인
    }

    /**
     * 알림 엔티티 생성, 저장
     *
     * @param notifiable 알림 인터페이스
     * @param gardener 알림 수신자
     * @return 저장된 알림 엔티티
     */
    private Notification save(Notifiable notifiable, Gardener gardener) {
        Notification notification = create(notifiable, gardener);
        return notificationRepository.save(notification);
    }

    /**
     * 알림 엔티티 생성
     * @param notifiable
     * @param gardener
     * @return
     */
    private Notification create(Notifiable notifiable, Gardener gardener) {
        NotificationType type = NotificationType.fromNotifiable(notifiable);

        // 각 알림 유형에 맞는 데이터를 매핑
        return new Notification(gardener, type,
                type == NotificationType.COMMENT ? (Comment) notifiable : null,
                type == NotificationType.FOLLOW ? (Follow) notifiable : null,
                type == NotificationType.LIKE ? (Like) notifiable : null,
                type == NotificationType.POST ? (Post) notifiable : null);
    }
}

