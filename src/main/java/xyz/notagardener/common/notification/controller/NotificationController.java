package xyz.notagardener.common.notification.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.notagardener.authentication.model.UserPrincipal;
import xyz.notagardener.common.notification.dto.DefaultNotification;
import xyz.notagardener.common.notification.service.NotificationService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/api/notification")
    public ResponseEntity<List<DefaultNotification>> getAll(@AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok().body(notificationService.getUnreadAll(user.getId()));
    }
}
