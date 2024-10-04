package xyz.notagardener.post.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import xyz.notagardener.common.error.code.ExceptionCode;
import xyz.notagardener.common.error.exception.ImageFailException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@Slf4j
public class ImageController {
    @Value("${upload.directory}")
    private String uploadDirectory;

    @GetMapping("/api/images/{filename}")
    public byte[] getImage(@PathVariable String filename) throws IOException {
        try {
            Path path = Paths.get(uploadDirectory, filename);
            return Files.readAllBytes(path); // 이미지 파일을 바이트 배열로 반환
        } catch (IOException e) {
            throw new ImageFailException(ExceptionCode.FAIL_TO_GET_IMAGE);
        }
    }
}
