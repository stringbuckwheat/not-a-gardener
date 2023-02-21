package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.FertilizerDto;

import java.util.List;

public interface FertilizerService {
    /* 비료 리스트 */
    List<FertilizerDto> getFertilizerList(int memberNo);

    // 비료 추가
    FertilizerDto addFertilizer(FertilizerDto fertilizerDto);

    // 비료 삭제
    void deleteFertilizer(int fertilizerNo);
}
