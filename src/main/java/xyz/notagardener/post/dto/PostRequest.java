package xyz.notagardener.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@AllArgsConstructor
@ToString
public class PostRequest {
    private String content;
    private List<MultipartFile> images; // 여러 장의 사진
}
