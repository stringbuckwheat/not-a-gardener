package xyz.notagardener.follow.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import xyz.notagardener.follow.dto.FollowRequest;
import xyz.notagardener.follow.service.FollowService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class FollowController {
    private final FollowService followService;

    @PostMapping("/api/follow")
    public ResponseEntity<Void> follow(@RequestBody @Valid FollowRequest followRequest) {
        followService.follow(followRequest);
        return ResponseEntity.noContent().build();
    }
}
