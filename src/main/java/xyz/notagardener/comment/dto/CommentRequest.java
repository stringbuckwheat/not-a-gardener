package xyz.notagardener.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CommentRequest {
    @NotBlank(message = "댓글 내용을 입력해주세요")
    private String content;

    @NotNull(message = "포스팅 정보를 비워둘 수 없습니다.")
    private Long postId;

    private Long parentCommentId;
}
