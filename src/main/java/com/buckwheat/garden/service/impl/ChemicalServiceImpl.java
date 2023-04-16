package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.dao.ChemicalDao;
import com.buckwheat.garden.data.dto.ChemicalDto;
import com.buckwheat.garden.data.dto.WateringDto;
import com.buckwheat.garden.data.entity.Chemical;
import com.buckwheat.garden.data.entity.Member;
import com.buckwheat.garden.data.entity.Watering;
import com.buckwheat.garden.service.ChemicalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChemicalServiceImpl implements ChemicalService {
    private final ChemicalDao chemicalDao;

    /**
     * 전체 chemical 리스트
     *
     * @param memberNo int FK로 조회
     * @return dto로 변환한 chemical list
     */
    @Override
    public List<ChemicalDto.Response> getChemicalList(int memberNo) {
        List<ChemicalDto.Response> chemicalList = new ArrayList<>();

        // entity -> dto
        for (Chemical chemical : chemicalDao.getChemicalListByMemberNo(memberNo)) {
            chemicalList.add(
                    ChemicalDto.Response.from(chemical)
            );
        }

        return chemicalList;
    }

    /**
     * 해당 chemical의 사용 내역
     *
     * @param chemicalNo PK
     * @return 해당 chemical의 사용내역 리스트
     */
    @Override
    public List<WateringDto.ResponseInChemical> getWateringListByChemical(int chemicalNo) {
        // @EntityGraph(attributePaths = {"wateringList", "wateringList.plant", "wateringList.plant.place"}, type = EntityGraph.EntityGraphType.FETCH)
        List<Watering> list = chemicalDao.getWateringListByChemicalNo(chemicalNo);
        List<WateringDto.ResponseInChemical> wateringList = new ArrayList<>();

        for (Watering watering : list) {
            wateringList.add(WateringDto.ResponseInChemical.from(watering));
        }

        return wateringList;
    }

    /**
     * chemical 추가
     *
     * @param chemicalRequest
     * @param member
     * @return insert된 chemical dto
     */
    @Override
    public ChemicalDto.Response addChemical(ChemicalDto.Request chemicalRequest, Member member) {
        Chemical chemical = chemicalDao.save(chemicalRequest.toEntityWithMember(member));
        return ChemicalDto.Response.from(chemical);
    }

    /**
     * chemical 수정
     *
     * @param chemicalRequest
     * @param member
     * @return 업데이트한 Chemical DTO
     */
    @Override
    public ChemicalDto.Response updateChemical(ChemicalDto.Request chemicalRequest, Member member) {
        Chemical chemical = chemicalRequest.toEntityWithMemberForUpdate(member);
        chemicalDao.save(chemical);
        return ChemicalDto.Response.from(chemical);
    }

    @Override
    public void deleteChemical(int chemicalNo) {
        chemicalDao.deleteByChemicalNo(chemicalNo);
    }
}
