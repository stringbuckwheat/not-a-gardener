package xyz.notagardener.repot.repot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.notagardener.repot.Repot;
import xyz.notagardener.repot.plant.dto.RepotList;
import xyz.notagardener.repot.repot.dto.RepotRequest;
import xyz.notagardener.repot.repot.dto.RepotResponse;
import xyz.notagardener.repot.repot.repository.RepotRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RepotServiceImpl implements RepotService {
    private final RepotCommandService repotCommandService;
    private final RepotRepository repotRepository;

    // 한 개 분갈이
    @Override
    @Transactional
    public RepotResponse addOne(RepotRequest repotRequest, Long gardenerId) {
        Repot repot = repotCommandService.addOne(repotRequest, gardenerId);
        return new RepotResponse(repot);
    }

    // 여러 개 한 번에 분갈이
    @Override
    @Transactional
    public List<RepotResponse> addAll(List<RepotRequest> requests, Long gardenerId) {
        return requests.stream()
                .map((request) -> addOne(request, gardenerId))
                .toList();
    }

    @Override
    public List<RepotList> getAll(Long gardenerId, Pageable pageable) {
        return repotRepository.findAll(gardenerId, pageable);
    }

    // TODO 물주기 캘린더 -> 정원기록 모듈
    // TODO 식물별 기록은 식물 서비스로 보내기 -> 식물 로그

    @Transactional
    @Override
    public RepotResponse update(RepotRequest repotRequest, Long gardenerId) {
        Repot repot = repotCommandService.update(repotRequest, gardenerId);
        return new RepotResponse(repot);
    }

    @Override
    public List<RepotResponse> updateAll(List<RepotRequest> repotRequests, Long gardenerId) {
        return repotRequests.stream().map((request) -> update(request, gardenerId)).toList();
    }

    @Override
    public void delete(Long repotId, Long gardenerId) {
        Repot repot = repotCommandService.getRepotByRepotIdAndGardenerId(repotId, gardenerId);
        repotRepository.delete(repot);
    }
}
