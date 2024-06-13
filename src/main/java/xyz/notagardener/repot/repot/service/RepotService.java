package xyz.notagardener.repot.repot.service;

import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import xyz.notagardener.repot.plant.dto.RepotList;
import xyz.notagardener.repot.repot.dto.RepotRequest;
import xyz.notagardener.repot.repot.dto.RepotResponse;

import java.util.List;

public interface RepotService {
    // 한 개 분갈이
    @Transactional
    RepotResponse addOne(RepotRequest repotRequest, Long gardenerId);

    // 여러 개 한 번에 분갈이
    @Transactional
    List<RepotResponse> addAll(List<RepotRequest> requests, Long gardenerId);

    List<RepotList> getAll(Long gardenerId, Pageable pageable);

    @Transactional
    RepotResponse update(RepotRequest repotRequest, Long gardenerId);

    List<RepotResponse> updateAll(List<RepotRequest> repotRequests, Long gardenerId);

    void delete(Long repotId, Long gardenerId);
}
