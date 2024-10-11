package xyz.notagardener.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.notagardener.comment.dto.CommentRequest;
import xyz.notagardener.comment.dto.CommentResponse;
import xyz.notagardener.comment.model.Comment;
import xyz.notagardener.comment.repository.CommentRepository;
import xyz.notagardener.common.error.code.ExceptionCode;
import xyz.notagardener.common.error.exception.ResourceNotFoundException;
import xyz.notagardener.common.error.exception.UnauthorizedAccessException;
import xyz.notagardener.gardener.model.Gardener;
import xyz.notagardener.gardener.repository.GardenerRepository;
import xyz.notagardener.notification.service.NotificationService;
import xyz.notagardener.post.model.Post;
import xyz.notagardener.post.repository.PostRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final GardenerRepository gardenerRepository;
    private final NotificationService notificationService;

    @Transactional
    public CommentResponse save(CommentRequest commentRequest, Long gardenerId) {
        Gardener commenter = gardenerRepository.findById(gardenerId)
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionCode.NO_SUCH_GARDENER));
        Post post = postRepository.findById(commentRequest.getPostId())
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionCode.NO_SUCH_POST));
        Comment parent = null;

        if (commentRequest.getParentCommentId() != null) {
            parent = commentRepository.findById(commentRequest.getParentCommentId())
                    .orElseThrow(() -> new ResourceNotFoundException(ExceptionCode.NO_SUCH_COMMENT));
        }

        Comment comment = commentRepository.save(
                Comment.builder()
                        .content(commentRequest.getContent())
                        .gardener(commenter)
                        .post(post)
                        .parent(parent)
                        .build()
        );

        // 본인 글에 단 댓글이 아닐 때만 알림 보내기
        if (!gardenerId.equals(post.getGardener().getGardenerId())) {
            notificationService.send(comment, post.getGardener().getGardenerId());
        }

        return new CommentResponse(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getAllBy(Long postId, Pageable pageable) {
        return commentRepository.findByPost_Id(postId, pageable).stream().map(CommentResponse::new).toList();
    }

    @Transactional
    public void delete(Long commentId, Long gardenerId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionCode.NO_SUCH_COMMENT));

        if (!comment.getGardener().getGardenerId().equals(gardenerId)) {
            throw new UnauthorizedAccessException(ExceptionCode.NOT_YOUR_COMMENT);
        }

        comment.delete();
    }
}
