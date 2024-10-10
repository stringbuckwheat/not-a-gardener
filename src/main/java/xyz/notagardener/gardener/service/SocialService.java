package xyz.notagardener.gardener.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.notagardener.common.error.code.ExceptionCode;
import xyz.notagardener.common.error.exception.ResourceNotFoundException;
import xyz.notagardener.gardener.dto.SocialGardenerInfo;
import xyz.notagardener.gardener.dto.SocialGardenerRequest;
import xyz.notagardener.gardener.dto.UserFeedResponse;
import xyz.notagardener.gardener.model.Gardener;
import xyz.notagardener.gardener.repository.GardenerRepository;
import xyz.notagardener.gardener.repository.SocialRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SocialService {
    private final SocialRepository socialRepository;
    private final GardenerRepository gardenerRepository;

    @Transactional(readOnly = true)
    public UserFeedResponse getProfile(String username, Long myId) {
        return socialRepository.getProfile(username, myId);
    }

    // 태그(@)용 회원 리스트
    @Transactional(readOnly = true)
    public List<SocialGardenerInfo> getTagInfoBy(String query, Long myId) {
        if(query == null || query.equals("")) {
            return socialRepository.getTagInfoBy(myId);
        }

        return socialRepository.getTagInfoBy(query, myId);
    }

    @Transactional
    public SocialGardenerInfo update(SocialGardenerRequest request, Long gardenerId) {
        Gardener gardener = gardenerRepository.findById(gardenerId)
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionCode.NO_SUCH_GARDENER));

        gardener.updateSocialInfo(request.getName(), request.getBiography());

        return new SocialGardenerInfo(gardener);
    }
}
