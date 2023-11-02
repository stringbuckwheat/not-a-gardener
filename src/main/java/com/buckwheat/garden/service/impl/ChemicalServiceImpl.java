package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.dao.ChemicalDao;
import com.buckwheat.garden.dao.WateringDao;
import com.buckwheat.garden.data.dto.chemical.ChemicalDetail;
import com.buckwheat.garden.data.dto.chemical.ChemicalDto;
import com.buckwheat.garden.data.dto.watering.WateringResponseInChemical;
import com.buckwheat.garden.service.ChemicalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChemicalServiceImpl implements ChemicalService {
    private final ChemicalDao chemicalDao;
    private final WateringDao wateringDao;

    /**
     * 전체 chemical 리스트
     *
     * @param gardenerId long FK로 조회
     * @return dto로 변환한 chemical list
     */
    @Override
    public List<ChemicalDto> readAll(Long gardenerId) {
        return chemicalDao.readAll(gardenerId);
    }

    /**
     * 해당 chemical의 상세 정보
     *
     * @param chemicalId PK
     * @return 해당 chemical의 상세 정보 및 사용내역 리스트
     */
    @Override
    public ChemicalDetail readOne(Long chemicalId, Long gardenerId) {
        ChemicalDto chemical = chemicalDao.readChemical(chemicalId, gardenerId);
        int wateringSize = wateringDao.getCountByChemical_ChemicalId(chemicalId);

        return new ChemicalDetail(chemical, wateringSize);
    }

    @Override
    public List<WateringResponseInChemical> readWateringsForChemical(Long chemicalId, Pageable pageable) {
        return wateringDao.getWateringsByChemicalIdWithPage(chemicalId, pageable)
                .stream()
                .map(WateringResponseInChemical::from)
                .collect(Collectors.toList());
    }

    @Override
    public ChemicalDto create(Long gardenerId, ChemicalDto chemicalRequest) {
        return chemicalDao.create(gardenerId, chemicalRequest);
    }

    @Override
    public ChemicalDto update(Long gardenerId, ChemicalDto chemicalRequest) {
        return chemicalDao.update(gardenerId, chemicalRequest);
    }

    @Override
    public void deactivate(Long chemicalId, Long gardenerId) {
        chemicalDao.deactivate(chemicalId, gardenerId);
    }
}
