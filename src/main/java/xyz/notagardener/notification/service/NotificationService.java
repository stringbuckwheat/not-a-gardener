package xyz.notagardener.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xyz.notagardener.like.dto.NotificationResponse;
import xyz.notagardener.like.repostiory.LikeRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {
    private final LikeRepository likeRepository;

    public List<NotificationResponse> getUnreadAll(Long gardenerId) {
        // TODO 댓글 구현 후 수정 필요
        return likeRepository.findByGardener_GardenerIdOrderByCreatedDateDesc(gardenerId).stream()
                .map(NotificationResponse::new).toList();
    }
}
