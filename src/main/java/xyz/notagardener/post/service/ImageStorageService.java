package xyz.notagardener.post.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import xyz.notagardener.common.error.code.ExceptionCode;
import xyz.notagardener.common.error.exception.ImageFailException;
import xyz.notagardener.post.model.PostImage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ImageStorageService {

    @Value("${upload.directory}")
    private String uploadDirectory;

    public List<PostImage> upload(List<MultipartFile> images) {
        List<PostImage> postImages = new ArrayList<>();

        for (MultipartFile image : images) {
            String urlPath = UUID.randomUUID().toString();
            String originalFileName = image.getOriginalFilename();
            Path path = Paths.get(uploadDirectory, urlPath); // 업로드 경로
            System.out.println(path);

            try {
                Files.createDirectories(path.getParent()); // 디렉토리 생성
                image.transferTo(path); // 파일 저장

                PostImage postImage = new PostImage(urlPath, originalFileName);
                postImages.add(postImage);
            } catch (IOException e) {
                throw new ImageFailException(ExceptionCode.FAIL_TO_UPLOAD_IMAGE);
            }
        }

        return postImages;
    }

    public void delete(String imageUrl) {
        Path path = Paths.get(uploadDirectory, imageUrl);
        System.out.println(path);

        try {
            Files.deleteIfExists(path); // 파일 삭제
        } catch (IOException e) {
            throw new ImageFailException(ExceptionCode.FAIL_TO_DELETE_IMAGE);
        }
    }
}
