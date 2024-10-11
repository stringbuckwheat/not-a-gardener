package xyz.notagardener.notification.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.notagardener.authentication.model.UserPrincipal;
import xyz.notagardener.notification.dto.NotificationResponse;
import xyz.notagardener.notification.service.NotificationService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/api/notification")
    public ResponseEntity<List<NotificationResponse>> getAll(@AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok().body(notificationService.getUnreadAll(user.getId()));
    }
}
