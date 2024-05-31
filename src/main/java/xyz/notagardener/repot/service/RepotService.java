package xyz.notagardener.repot.service;

import xyz.notagardener.repot.dto.RepotRequest;
import xyz.notagardener.repot.dto.RepotResponse;

import java.util.List;

public interface RepotService {
    RepotResponse addOne(RepotRequest repotRequest, Long gardenerId);
    List<RepotResponse> addAll(List<RepotRequest> requests, Long gardenerId);
    RepotResponse update(RepotRequest repotRequest, Long gardenerId);
    List<RepotResponse> updateAll(List<RepotRequest> repotRequests, Long gardenerId);
    void delete(Long repotId, Long gardenerId);
}