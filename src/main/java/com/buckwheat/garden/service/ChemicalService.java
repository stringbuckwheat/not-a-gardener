package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.ChemicalDto;

import java.util.List;

public interface ChemicalService {
    /* 약품(비료, 살균, 살충제) 리스트 */
    List<ChemicalDto.Response> getActivatedChemicalsByGardenerId(Long gardenerId);

    /* 약품(비료, 살균, 살충제) 사용 내역 */
    ChemicalDto.Detail getChemicalByChemicalId(Long chemicalId);

    /* 약품(비료, 살균, 살충제) 추가 */
    ChemicalDto.Response add(Long gardenerId, ChemicalDto.Request chemicalRequest);

    /* 약품(비료, 살균, 살충제) 수정 */
    ChemicalDto.Response modify(Long gardenerId, ChemicalDto.Request chemicalRequest);

    /* 약품(비료, 살균, 살충제) 삭제 */
    void deactivateChemical(Long chemicalId);
}
