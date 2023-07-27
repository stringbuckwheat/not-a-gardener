package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.ChemicalDto;

import java.util.List;

public interface ChemicalService {
    /* 약품(비료, 살균, 살충제) 리스트 */
    List<ChemicalDto.Basic> getAll(Long gardenerId);

    /* 약품(비료, 살균, 살충제) 사용 내역 */
    ChemicalDto.Detail getDetail(Long chemicalId, Long gardenerId);

    /* 약품(비료, 살균, 살충제) 추가 */
    ChemicalDto.Basic add(Long gardenerId, ChemicalDto.Basic chemicalRequest);

    /* 약품(비료, 살균, 살충제) 수정 */
    ChemicalDto.Basic modify(Long gardenerId, ChemicalDto.Basic chemicalRequest);

    /* 약품(비료, 살균, 살충제) 삭제 */
    void deactivate(Long chemicalId, Long gardenerId);
}
