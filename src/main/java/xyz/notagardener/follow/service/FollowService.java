package xyz.notagardener.follow.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xyz.notagardener.common.error.code.ExceptionCode;
import xyz.notagardener.common.error.exception.ResourceNotFoundException;
import xyz.notagardener.common.error.exception.SelfFollowException;
import xyz.notagardener.common.notification.dto.FollowNotification;
import xyz.notagardener.common.notification.service.NotificationService;
import xyz.notagardener.follow.dto.FollowRequest;
import xyz.notagardener.follow.model.Follow;
import xyz.notagardener.follow.repository.FollowRepository;
import xyz.notagardener.gardener.model.Gardener;
import xyz.notagardener.gardener.repository.GardenerRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;
    private final GardenerRepository gardenerRepository;
    private final NotificationService notificationService;

    public void follow(FollowRequest request) {
        // 셀프 팔로우 금지
        if (request.getFollowingId().equals(request.getFollowerId())) {
            throw new SelfFollowException(ExceptionCode.CANNOT_FOLLOW_YOURSELF);
        }

        followRepository.findByFollower_GardenerIdAndFollowing_GardenerId(request.getFollowerId(), request.getFollowingId())
                .ifPresentOrElse(
                        followRepository::delete,  // 있으면 언팔로우
                        () -> createFollow(request)  // 없으면 팔로우
                );
    }

    private void createFollow(FollowRequest followRequest) {
        // 팔로우 한 사람
        Gardener follower = gardenerRepository.findById(followRequest.getFollowerId())
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionCode.NO_SUCH_GARDENER));

        // 팔로우 당한 사람
        Gardener following = gardenerRepository.getReferenceById(followRequest.getFollowingId());

        Follow newFollow = followRepository.save(new Follow(follower, following));
        sendNotification(newFollow, followRequest.getFollowingId());
    }

    private void sendNotification(Follow follow, Long followingId) {
        // 웹소켓 알림
        FollowNotification notification = new FollowNotification(follow);
        notificationService.sendFollowNotification(notification, followingId);
    }
}
