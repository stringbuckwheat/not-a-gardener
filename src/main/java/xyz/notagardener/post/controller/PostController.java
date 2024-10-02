package xyz.notagardener.post.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xyz.notagardener.authentication.model.UserPrincipal;
import xyz.notagardener.post.dto.PostRequest;
import xyz.notagardener.post.dto.PostResponse;
import xyz.notagardener.post.service.PostService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PostController {
    private final PostService postService;

    @GetMapping("/api/post")
    public ResponseEntity<List<PostResponse>> getAll(@PageableDefault(page = 0, size = 10) Pageable pageable,
                                                     @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok().body(postService.getAll(pageable, user.getId()));
    }

    @GetMapping("/api/post/{id}")
    public ResponseEntity<PostResponse> getOne(@PathVariable(name = "id") Long postId,
                                               @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok().body(postService.getOne(postId, user.getId()));
    }

    @PostMapping("/api/post")
    public ResponseEntity<PostResponse> save(@RequestParam("content") String content,
                                             @RequestParam("images") List<MultipartFile> images,
                                             @AuthenticationPrincipal UserPrincipal user) {
        PostRequest postRequest = new PostRequest(content, images);
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.save(postRequest, user.getId()));
    }

    @DeleteMapping("/api/post/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") Long postId,
                                       @AuthenticationPrincipal UserPrincipal user) {
        postService.delete(postId, user.getId());
        return ResponseEntity.noContent().build();
    }
}
