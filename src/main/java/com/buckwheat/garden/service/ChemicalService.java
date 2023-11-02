package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.chemical.ChemicalDetail;
import com.buckwheat.garden.data.dto.chemical.ChemicalDto;
import com.buckwheat.garden.data.dto.watering.WateringResponseInChemical;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ChemicalService {
    /* 약품(비료, 살균, 살충제) 리스트 */
    List<ChemicalDto> readAll(Long gardenerId);

    /* 약품(비료, 살균, 살충제) 사용 내역 */
    ChemicalDetail readOne(Long chemicalId, Long gardenerId);

    /* 약품 사용내역 페이징 */
    List<WateringResponseInChemical> readWateringsForChemical(Long chemicalId, Pageable pageable);

    /* 약품(비료, 살균, 살충제) 추가 */
    ChemicalDto create(Long gardenerId, ChemicalDto chemicalRequest);

    /* 약품(비료, 살균, 살충제) 수정 */
    ChemicalDto update(Long gardenerId, ChemicalDto chemicalRequest);

    /* 약품(비료, 살균, 살충제) 삭제 */
    void deactivate(Long chemicalId, Long gardenerId);
}
