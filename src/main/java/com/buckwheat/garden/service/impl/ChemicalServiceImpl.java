package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.dao.ChemicalDao;
import com.buckwheat.garden.data.dto.ChemicalDto;
import com.buckwheat.garden.data.dto.WateringDto;
import com.buckwheat.garden.data.entity.Chemical;
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
     * @param gardenerId long FK로 조회
     * @return dto로 변환한 chemical list
     */
    @Override
    public List<ChemicalDto.Basic> getAll(Long gardenerId) {
        List<ChemicalDto.Basic> chemicalList = new ArrayList<>();

        // entity -> dto
        for (Chemical chemical : chemicalDao.getActivatedChemicalsByGardenerId(gardenerId)) {
            chemicalList.add(ChemicalDto.Basic.from(chemical));
        }

        return chemicalList;
    }

    /**
     * 해당 chemical의 상세 정보
     *
     * @param chemicalId PK
     * @return 해당 chemical의 상세 정보 및 사용내역 리스트
     */
    @Override
    public ChemicalDto.Detail getDetail(Long chemicalId, Long gardenerId) {
        Chemical chemical = chemicalDao.getChemicalByChemicalIdAndGardenerId(chemicalId, gardenerId);

        List<WateringDto.ResponseInChemical> waterings = new ArrayList<>();

        for (Watering watering : chemical.getWaterings()) {
            waterings.add(WateringDto.ResponseInChemical.from(watering));
        }

        return new ChemicalDto.Detail(ChemicalDto.Basic.from(chemical), waterings);
    }

    @Override
    public ChemicalDto.Basic add(Long gardenerId, ChemicalDto.Basic chemicalRequest) {
        return ChemicalDto.Basic.from(chemicalDao.save(gardenerId, chemicalRequest));
    }

    @Override
    public ChemicalDto.Basic modify(Long gardenerId, ChemicalDto.Basic chemicalRequest) {
        return ChemicalDto.Basic.from(chemicalDao.update(gardenerId, chemicalRequest));
    }

    @Override
    public void deactivate(Long chemicalId, Long gardenerId) {
        chemicalDao.deactivateChemical(chemicalId, gardenerId);
    }
}
