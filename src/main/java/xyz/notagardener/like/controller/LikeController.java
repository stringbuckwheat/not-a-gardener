package xyz.notagardener.like.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.notagardener.authentication.model.UserPrincipal;
import xyz.notagardener.like.dto.LikeResponse;
import xyz.notagardener.like.service.LikeService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LikeController {
    private final LikeService likeService;

    @PostMapping("/api/post/{id}/like")
    public ResponseEntity<LikeResponse> switchLike(@PathVariable(name = "id") Long postId,
                                                   @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok().body(likeService.like(postId, user.getId()));
    }

    @PutMapping("/api/post/like/{id}")
    public ResponseEntity<Void> readNotification(@PathVariable(name = "id") Long likeId) {
        likeService.readNotification(likeId);
        return ResponseEntity.noContent().build();
    }
}
