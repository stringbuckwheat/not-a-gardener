package xyz.notagardener.comment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import xyz.notagardener.authentication.model.UserPrincipal;
import xyz.notagardener.comment.dto.CommentRequest;
import xyz.notagardener.comment.dto.CommentResponse;
import xyz.notagardener.comment.service.CommentService;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/api/post/{id}/comment")
    public ResponseEntity<List<CommentResponse>> getAllBy(@PathVariable(name = "id") Long postId,
                                                          @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok().body(commentService.getAllBy(postId, pageable));
    }

    @PostMapping("/api/post/{id}/comment")
    public ResponseEntity<CommentResponse> save(@RequestBody @Valid CommentRequest commentRequest,
                                                @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.save(commentRequest, user.getId()));
    }

    @DeleteMapping("/api/post/comment/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") Long commentId,
                                       @AuthenticationPrincipal UserPrincipal user) {
        commentService.delete(commentId, user.getId());
        return ResponseEntity.noContent().build();
    }
}
