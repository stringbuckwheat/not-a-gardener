package xyz.notagardener.notification.enums;

import xyz.notagardener.comment.model.Comment;
import xyz.notagardener.common.model.Notifiable;
import xyz.notagardener.follow.model.Follow;
import xyz.notagardener.like.entity.Like;
import xyz.notagardener.post.model.Post;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum NotificationType {
    LIKE(Like.class),
    COMMENT(Comment.class),
    FOLLOW(Follow.class),
    POST(Post.class);

    private static final Map<Class<? extends Notifiable>, NotificationType> typeMap =
            Collections.unmodifiableMap(
                    Stream.of(values())
                            .collect(Collectors.toMap(NotificationType::getNotifiableClass, type -> type))
            );

    private final Class<? extends Notifiable> notifiableClass;

    NotificationType(Class<? extends Notifiable> notifiableClass) {
        this.notifiableClass = notifiableClass;
    }

    public Class<? extends Notifiable> getNotifiableClass() {
        return notifiableClass;
    }

    public static NotificationType fromNotifiable(Notifiable notifiable) {
        return typeMap.get(notifiable.getClass());
    }
}

