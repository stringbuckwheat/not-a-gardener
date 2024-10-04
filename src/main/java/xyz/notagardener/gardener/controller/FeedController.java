package xyz.notagardener.gardener.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import xyz.notagardener.gardener.dto.UserFeedResponse;
import xyz.notagardener.gardener.service.FeedService;

@RestController
@RequiredArgsConstructor
public class FeedController {
    private final FeedService feedService;

    @GetMapping("/api/feed/gardener/{username}")
    public ResponseEntity<UserFeedResponse> getFeed(@PathVariable(name = "username") String username) {
        return ResponseEntity.ok().body(feedService.getFeed(username));
    }
}
