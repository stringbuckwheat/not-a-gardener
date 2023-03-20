package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.ChemicalDto;
import com.buckwheat.garden.data.dto.WateringDto;
import com.buckwheat.garden.data.entity.Member;
import com.buckwheat.garden.data.entity.Watering;

import java.util.List;

public interface ChemicalService {
    /* 약품(비료, 살균, 살충제) 리스트 */
    List<ChemicalDto.ChemicalResponse> getChemicalList(int memberNo);

    /* 약품(비료, 살균, 살충제) 사용 내역 */
    List<WateringDto.WateringResponseInChemical> getWateringListByChemical(int chemicalNo);

    /* 약품(비료, 살균, 살충제) 추가 */
    ChemicalDto.ChemicalResponse addChemical(ChemicalDto.ChemicalRequest chemicalRequest, Member member);

    /* 약품(비료, 살균, 살충제) 수정 */
    ChemicalDto.ChemicalResponse updateChemical(ChemicalDto.ChemicalRequest chemicalRequest, Member member);

    /* 약품(비료, 살균, 살충제) 삭제 */
    void deleteChemical(int chemicalNo);
}
