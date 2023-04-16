package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.ChemicalDto;
import com.buckwheat.garden.data.dto.WateringDto;
import com.buckwheat.garden.data.entity.Member;

import java.util.List;

public interface ChemicalService {
    /* 약품(비료, 살균, 살충제) 리스트 */
    List<ChemicalDto.Response> getChemicalList(int memberNo);

    /* 약품(비료, 살균, 살충제) 사용 내역 */
    List<WateringDto.ResponseInChemical> getWateringListByChemical(int chemicalNo);

    /* 약품(비료, 살균, 살충제) 추가 */
    ChemicalDto.Response addChemical(ChemicalDto.Request chemicalRequest, Member member);

    /* 약품(비료, 살균, 살충제) 수정 */
    ChemicalDto.Response updateChemical(ChemicalDto.Request chemicalRequest, Member member);

    /* 약품(비료, 살균, 살충제) 삭제 */
    void deleteChemical(int chemicalNo);
}
