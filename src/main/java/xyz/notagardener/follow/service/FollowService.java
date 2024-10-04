package xyz.notagardener.follow.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xyz.notagardener.common.error.code.ExceptionCode;
import xyz.notagardener.common.error.exception.ResourceNotFoundException;
import xyz.notagardener.follow.dto.FollowRequest;
import xyz.notagardener.follow.model.Follow;
import xyz.notagardener.follow.repository.FollowRepository;
import xyz.notagardener.gardener.model.Gardener;
import xyz.notagardener.gardener.repository.GardenerRepository;
import xyz.notagardener.common.notification.dto.FollowNotification;
import xyz.notagardener.common.notification.service.NotificationService;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;
    private final GardenerRepository gardenerRepository;
    private final NotificationService notificationService;

    public void follow(FollowRequest followRequest) {
        Optional<Follow> follow = followRepository.findByFollower_GardenerIdAndFollowing_GardenerId(
                followRequest.getFollowerId(), followRequest.getFollowingId());

        if(follow.isPresent()) {
            followRepository.delete(follow.get());
        } else {
            // 없으면 save

            // 팔로우 한 사람
            Gardener follower = gardenerRepository.findById(followRequest.getFollowerId())
                    .orElseThrow(() -> new ResourceNotFoundException(ExceptionCode.NO_SUCH_GARDENER));

            // 팔로우 당한 사람
            Gardener following = gardenerRepository.getReferenceById(followRequest.getFollowingId());

            Follow newFollow = followRepository.save(new Follow(follower, following));

            // 웹소켓 알림
            FollowNotification notification = new FollowNotification(newFollow);
            notificationService.sendFollowNotification(notification, followRequest.getFollowingId());
        }
    }
}
