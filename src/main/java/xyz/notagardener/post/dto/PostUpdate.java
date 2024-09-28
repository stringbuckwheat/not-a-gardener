package xyz.notagardener.post.dto;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
public class PostUpdate {
    private String content;
    private List<MultipartFile> newImages;
    private List<String> removeImageIds;
}
