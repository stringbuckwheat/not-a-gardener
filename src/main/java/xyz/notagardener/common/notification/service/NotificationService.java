package xyz.notagardener.common.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import xyz.notagardener.common.notification.dto.DefaultNotification;
import xyz.notagardener.common.notification.dto.FollowNotification;
import xyz.notagardener.like.repostiory.LikeRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {
    private final LikeRepository likeRepository;
    private final SimpMessagingTemplate messagingTemplate; // 웹소켓 메시지 전송


    public List<DefaultNotification> getUnreadAll(Long gardenerId) {
        // TODO 댓글, 팔로우 등 구현 후 수정
        return likeRepository.findByGardener_GardenerIdOrderByCreatedDateDesc(gardenerId).stream()
                .map(DefaultNotification::new).toList();
    }

    public void sendLikeNotification(DefaultNotification defaultNotification, Long postOwnerId) {
        messagingTemplate.convertAndSend(
                "/queue/notifications/" + postOwnerId,
                defaultNotification);
    }

    public void sendFollowNotification(FollowNotification followNotification, Long followingId) {
        messagingTemplate.convertAndSend(
                "/queue/notifications/" + followingId,
                followNotification);
    }
}
