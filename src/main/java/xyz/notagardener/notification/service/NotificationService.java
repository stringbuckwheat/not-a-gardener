package xyz.notagardener.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import xyz.notagardener.notification.dto.NotificationResponse;
import xyz.notagardener.like.repostiory.LikeRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {
    private final LikeRepository likeRepository;
    private final SimpMessagingTemplate messagingTemplate; // 웹소켓 메시지 전송


    public List<NotificationResponse> getUnreadAll(Long gardenerId) {
        // TODO 댓글, 팔로우 등 구현 후 수정
        return likeRepository.findByGardener_GardenerIdOrderByCreatedDateDesc(gardenerId).stream()
                .map(NotificationResponse::new).toList();
    }

    public void sendLikeNotification(NotificationResponse notificationResponse, Long postOwnerId) {
        messagingTemplate.convertAndSend(
                "/queue/notifications/" + postOwnerId,
                notificationResponse);
    }
}
