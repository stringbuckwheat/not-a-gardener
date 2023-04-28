package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.dao.ChemicalDao;
import com.buckwheat.garden.dao.GardenerDao;
import com.buckwheat.garden.data.dto.ChemicalDto;
import com.buckwheat.garden.data.dto.WateringDto;
import com.buckwheat.garden.data.entity.Chemical;
import com.buckwheat.garden.data.entity.Gardener;
import com.buckwheat.garden.data.entity.Watering;
import com.buckwheat.garden.service.ChemicalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChemicalServiceImpl implements ChemicalService {
    private final ChemicalDao chemicalDao;
    private final GardenerDao gardenerDao;

    /**
     * 전체 chemical 리스트
     *
     * @param GardenerId int FK로 조회
     * @return dto로 변환한 chemical list
     */
    @Override
    public List<ChemicalDto.Response> getChemicalsByGardenerId(Long gardenerId) {
        List<ChemicalDto.Response> chemicalList = new ArrayList<>();

        // entity -> dto
        for (Chemical chemical : chemicalDao.getChemicalsByGardenerId(gardenerId)) {
            chemicalList.add(
                    ChemicalDto.Response.from(chemical)
            );
        }

        return chemicalList;
    }

    /**
     * 해당 chemical의 사용 내역
     *
     * @param chemicalId PK
     * @return 해당 chemical의 사용내역 리스트
     */
    @Override
    public List<WateringDto.ResponseInChemical> getWateringsByChemicalId(Long chemicalId) {
        List<WateringDto.ResponseInChemical> wateringList = new ArrayList<>();

        for (Watering watering : chemicalDao.getWateringsByChemicalId(chemicalId)) {
            wateringList.add(WateringDto.ResponseInChemical.from(watering));
        }

        return wateringList;
    }

    @Override
    public ChemicalDto.Response add(Long gardenerId, ChemicalDto.Request chemicalRequest) {
        Gardener gardener = gardenerDao.getGardenerByGardenerId(gardenerId).orElseThrow(NoSuchElementException::new);
        Chemical chemical = chemicalDao.save(chemicalRequest.toEntityWithGardener(gardener));
        return ChemicalDto.Response.from(chemical);
    }

    @Override
    public ChemicalDto.Response modify(Long gardenerId, ChemicalDto.Request chemicalRequest) {
        Gardener gardener = gardenerDao.getGardenerByGardenerId(gardenerId).orElseThrow(NoSuchElementException::new);
        Chemical chemical = chemicalRequest.toEntityWithGardenerForUpdate(gardener);
        chemicalDao.save(chemical);
        return ChemicalDto.Response.from(chemical);
    }

    @Override
    public void delete(Long id) {
        chemicalDao.deleteByChemicalId(id);
    }
}
