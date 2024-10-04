package xyz.notagardener.like.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.notagardener.common.error.code.ExceptionCode;
import xyz.notagardener.common.error.exception.ResourceNotFoundException;
import xyz.notagardener.gardener.model.Gardener;
import xyz.notagardener.gardener.repository.GardenerRepository;
import xyz.notagardener.like.dto.LikeResponse;
import xyz.notagardener.common.notification.dto.DefaultNotification;
import xyz.notagardener.like.entity.Like;
import xyz.notagardener.like.repostiory.LikeRepository;
import xyz.notagardener.common.notification.service.NotificationService;
import xyz.notagardener.post.model.Post;
import xyz.notagardener.post.repository.PostRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LikeService {
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final GardenerRepository gardenerRepository;
    private final NotificationService notificationService;

    // c, d
    public LikeResponse like(Long postId, Long gardenerId) {
        // 이미 좋아요를 누른 상태라면 삭제
        Optional<Like> like = likeRepository.findByGardener_GardenerIdAndPost_Id(gardenerId, postId);

        if (like.isPresent()) {
            likeRepository.delete(like.get());
        } else {
            // 없으면 INSERT
            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new ResourceNotFoundException(ExceptionCode.NO_SUCH_POST));
            Gardener likedGardener = gardenerRepository.findById(gardenerId)
                    .orElseThrow(() -> new ResourceNotFoundException(ExceptionCode.NO_SUCH_GARDENER));
            Like newLike = likeRepository.save(new Like(post, likedGardener));

            // 웹소켓 알림
            DefaultNotification notification = new DefaultNotification(newLike);

            Long postOwnerId = post.getGardener().getGardenerId();
            notificationService.sendLikeNotification(notification, postOwnerId);
        }

        // Response 만들기
        Long likeCount = likeRepository.countByPost_Id(postId);
        return new LikeResponse(likeCount, like.isEmpty());
    }

    public LikeResponse getOne(Long postId, Long gardenerId) {
        Optional<Like> like = likeRepository.findByGardener_GardenerIdAndPost_Id(gardenerId, postId);
        Long likeCount = likeRepository.countByPost_Id(postId);
        return new LikeResponse(likeCount, like.isPresent());
    }

    @Transactional
    public void readNotification(Long likeId) {
        likeRepository.findById(likeId).ifPresent(Like::readNotification);
    }
}
