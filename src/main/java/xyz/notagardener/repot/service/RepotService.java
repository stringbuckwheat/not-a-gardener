package xyz.notagardener.repot.service;

import org.springframework.data.domain.Pageable;
import xyz.notagardener.plant.plant.dto.RepotList;
import xyz.notagardener.repot.dto.RepotRequest;
import xyz.notagardener.repot.dto.RepotResponse;

import java.util.List;

public interface RepotService {
    RepotResponse addOne(RepotRequest repotRequest, Long gardenerId);
    List<RepotResponse> addAll(List<RepotRequest> requests, Long gardenerId);
    List<RepotList> getAll(Long gardenerId, Pageable pageable);
    RepotResponse update(RepotRequest repotRequest, Long gardenerId);
    List<RepotResponse> updateAll(List<RepotRequest> repotRequests, Long gardenerId);
    void delete(Long repotId, Long gardenerId);
}