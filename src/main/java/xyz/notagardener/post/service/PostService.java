package xyz.notagardener.post.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.notagardener.common.error.code.ExceptionCode;
import xyz.notagardener.common.error.exception.ResourceNotFoundException;
import xyz.notagardener.common.error.exception.UnauthorizedAccessException;
import xyz.notagardener.notification.service.NotificationService;
import xyz.notagardener.follow.model.Follow;
import xyz.notagardener.gardener.model.Gardener;
import xyz.notagardener.like.dto.LikeResponse;
import xyz.notagardener.like.service.LikeService;
import xyz.notagardener.post.dto.PostOverview;
import xyz.notagardener.post.dto.PostRequest;
import xyz.notagardener.post.dto.PostResponse;
import xyz.notagardener.post.dto.PostUpdate;
import xyz.notagardener.post.model.Post;
import xyz.notagardener.post.model.PostImage;
import xyz.notagardener.post.repository.PostRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {
    private final ImageStorageService imageStorageService;
    private final LikeService likeService;
    private final PostRepository postRepository;
    private final NotificationService notificationService;

    @Transactional(readOnly = true)
    public List<PostResponse> getAll(Pageable pageable, Long gardenerId) {
        return postRepository.getAllBy(pageable, gardenerId);
    }

    @Transactional(readOnly = true)
    public PostResponse getOne(Long postId, Long gardenerId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException(ExceptionCode.NO_SUCH_POST));

        LikeResponse like = likeService.getOne(postId, gardenerId);
        return new PostResponse(post, like);
    }

    @Transactional(readOnly = true)
    public List<PostOverview> getOverviews(Pageable pageable, String username) {
        return postRepository.getOverviews(pageable, username).stream().map(PostOverview::new).toList();
    }

    public PostResponse save(PostRequest postRequest, Long gardenerId) {
        // 여러 장의 사진 업로드
        Gardener gardener = postRepository.getGardener(gardenerId)
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionCode.NO_SUCH_GARDENER));

        Post post = new Post(postRequest.getContent(), gardener);

        List<PostImage> imageUrls = imageStorageService.upload(postRequest.getImages());

        // 이미지 엔티티 생성 및 부모와의 관계 설정
        for (PostImage image: imageUrls) {
            image.setPost(post);
            post.getImages().add(image);
        }

        // 웹소켓 알림
        List<Long> followerIds = gardener.getFollowers().stream().map(Follow::getFollowerId).toList();
        notificationService.send(post, followerIds);

        return new PostResponse(postRepository.save(post), 0L, false);
    }

    @Transactional
    public PostResponse update(PostUpdate postRequest, Long postId, Long gardnerId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionCode.NO_SUCH_POST));

        if(!post.getGardener().getGardenerId().equals(gardnerId)) {
            throw new UnauthorizedAccessException(ExceptionCode.NOT_YOUR_POST);
        }

        // 게시글 내용 업데이트
        post.setContent(postRequest.getContent());

        // 기존 이미지 처리
        if (postRequest.getRemoveImageIds() != null && !postRequest.getRemoveImageIds().isEmpty()) {
            // 사용자가 삭제 요청한 이미지들 삭제
            List<PostImage> imagesToRemove = post.getImages().stream()
                    .filter(image -> postRequest.getRemoveImageIds().contains(image.getId()))
                    .toList();

            // 이미지 실제 파일 삭제 및 DB에서 제거
            for (PostImage image : imagesToRemove) {
                imageStorageService.delete(image.getUrl());
                post.getImages().remove(image); // 엔티티에서 삭제
            }
        }

        // 새로운 이미지 업로드 처리
        if (postRequest.getNewImages() != null && !postRequest.getNewImages().isEmpty()) {
            List<PostImage> newImages = imageStorageService.upload(postRequest.getNewImages());
            for (PostImage newImage : newImages) {
                newImage.setPost(post);
                post.getImages().add(newImage);
            }
        }

        // 게시글 저장 후 응답 반환
        LikeResponse like = likeService.getOne(postId, gardnerId);
        return new PostResponse(post, like);
    }


    // d
    public void delete(Long postId, Long gardenerId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionCode.NO_SUCH_POST));

        if(!post.getGardener().getGardenerId().equals(gardenerId)) {
            throw new UnauthorizedAccessException(ExceptionCode.NOT_YOUR_POST);
        }

        // 이미지 파일 삭제
        for (PostImage image : post.getImages()) {
            imageStorageService.delete(image.getUrl());
        }

        // 엔티티 삭제
        postRepository.delete(post);
    }
}

