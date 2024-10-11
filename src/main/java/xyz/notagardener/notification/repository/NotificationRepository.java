package xyz.notagardener.notification.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import xyz.notagardener.notification.model.Notification;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @EntityGraph(attributePaths = {"comment", "follow", "like", "post", "post.images", "gardener"})
    List<Notification> findByGardener_GardenerId(Long gardenerId);
}
