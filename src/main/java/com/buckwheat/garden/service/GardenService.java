package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.GardenDto;

import java.time.LocalDate;
import java.util.List;

public interface GardenService {
    /* 해당 유저의 전체 식물과 가장 최근 물/비료 준 날짜 리스트 반환 */
    List<GardenDto> getGarden(int memberNo);
}