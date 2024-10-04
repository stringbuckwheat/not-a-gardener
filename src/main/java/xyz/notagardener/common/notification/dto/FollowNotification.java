package xyz.notagardener.common.notification.dto;

import lombok.Getter;
import xyz.notagardener.follow.model.Follow;
import xyz.notagardener.gardener.model.Gardener;
import xyz.notagardener.common.notification.enums.NotificationType;

import java.time.LocalDateTime;

@Getter
public class FollowNotification extends Notification{
    private String name;
    private String username;
    private LocalDateTime followDate;

    public FollowNotification(Follow follow) {
        super(NotificationType.FOLLOW);

        Gardener follower = follow.getFollower();

        this.name = follower.getName();
        this.username = follower.getUsername();
        this.followDate = follow.getFollowDate();
    }
}
