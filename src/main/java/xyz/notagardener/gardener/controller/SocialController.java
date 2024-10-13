package xyz.notagardener.gardener.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import xyz.notagardener.authentication.model.UserPrincipal;
import xyz.notagardener.gardener.dto.SocialGardenerInfo;
import xyz.notagardener.gardener.dto.SocialGardenerRequest;
import xyz.notagardener.gardener.dto.UserFeedResponse;
import xyz.notagardener.gardener.service.SocialService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SocialController {
    private final SocialService socialService;

    @GetMapping("/api/social/gardener/{username}")
    public ResponseEntity<UserFeedResponse> getProfile(@PathVariable(name = "username") String username,
                                                       @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok().body(socialService.getProfile(username, user.getId()));
    }

    @GetMapping("/api/social/gardener")
    public ResponseEntity<List<SocialGardenerInfo>> getTagInfoBy(@RequestParam(name = "username") String username,
                                                        @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok().body(socialService.getTagInfoBy(username, user.getId()));
    }

    @PostMapping("/api/social/gardener")
    public ResponseEntity<SocialGardenerInfo> update(@RequestBody @Valid SocialGardenerRequest request,
                                                     @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok().body(socialService.update(request, user.getId()));
    }
}
