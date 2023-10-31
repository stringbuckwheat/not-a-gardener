package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.dao.ChemicalDao;
import com.buckwheat.garden.dao.WateringDao;
import com.buckwheat.garden.data.dto.chemical.ChemicalDetail;
import com.buckwheat.garden.data.dto.chemical.ChemicalDto;
import com.buckwheat.garden.data.dto.watering.WateringResponseInChemical;
import com.buckwheat.garden.data.entity.Chemical;
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
    public List<ChemicalDto> getAll(Long gardenerId) {
        return chemicalDao.getActivatedChemicalsByGardenerId(gardenerId).stream()
                .map(ChemicalDto::from)
                .collect(Collectors.toList());
    }

    /**
     * 해당 chemical의 상세 정보
     *
     * @param chemicalId PK
     * @return 해당 chemical의 상세 정보 및 사용내역 리스트
     */
    @Override
    public ChemicalDetail getDetail(Long chemicalId, Long gardenerId) {
        Chemical chemical = chemicalDao.getChemicalByChemicalIdAndGardenerId(chemicalId, gardenerId);
        Long wateringSize = wateringDao.getCountByChemical_ChemicalId(chemicalId);

        return new ChemicalDetail(ChemicalDto.from(chemical), wateringSize);
    }

    @Override
    public List<WateringResponseInChemical> getWateringWithPaging(Long chemicalId, Pageable pageable) {
        return wateringDao.getWateringsByChemicalIdWithPage(chemicalId, pageable)
                .stream()
                .map(WateringResponseInChemical::from)
                .collect(Collectors.toList());
    }

    @Override
    public ChemicalDto add(Long gardenerId, ChemicalDto chemicalRequest) {
        return ChemicalDto.from(chemicalDao.save(gardenerId, chemicalRequest));
    }

    @Override
    public ChemicalDto modify(Long gardenerId, ChemicalDto chemicalRequest) {
        return ChemicalDto.from(chemicalDao.update(gardenerId, chemicalRequest));
    }

    @Override
    public void deactivate(Long chemicalId, Long gardenerId) {
        chemicalDao.deactivateChemical(chemicalId, gardenerId);
    }
}
