package xyz.notagardener.comment.dto;

import lombok.Getter;
import xyz.notagardener.comment.model.Comment;

import java.time.LocalDateTime;

@Getter
public class CommentResponse {
    private Long id;
    private String content;
    private Long commenterId;
    private String username;
    private String name;
    private LocalDateTime createdDate;

    public CommentResponse(Comment comment) {
        this.id = comment.getId();
        this.createdDate = comment.getCreatedDate();

        // 삭제된 댓글
        if(comment.getDeletedDate() != null) {
            this.content = "삭제된 댓글입니다";
            this.username = "";
            this.name = "";
        } else {
            this.content = comment.getContent();
            this.commenterId = comment.getGardener().getGardenerId();
            this.username = comment.getGardener().getUsername();
            this.name = comment.getGardener().getName();
        }
    }
}
