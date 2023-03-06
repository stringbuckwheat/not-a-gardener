package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.data.dto.PesticideDateDto;
import com.buckwheat.garden.data.entity.Pesticide;
import com.buckwheat.garden.data.entity.PesticideDate;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.repository.PesticideDateRepository;
import com.buckwheat.garden.repository.PesticideRepository;
import com.buckwheat.garden.repository.PlantRepository;
import com.buckwheat.garden.service.PesticideDateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class PesticideDateServiceImpl implements PesticideDateService {
    private PesticideDateRepository pesticideDateRepository;
    private PlantRepository plantRepository;
    private PesticideRepository pesticideRepository;

    @Override
    @Transactional(readOnly = true)
    public List<PesticideDateDto> getPesticideDateListByPesticide(int pesticideNo) {
        List<PesticideDateDto> pesticideDateDtoList = new ArrayList<>();

        // entity -> dto로 변환
        for(PesticideDate p : pesticideDateRepository.findByPesticide_PesticideNoOrderByPesticideDateDesc(pesticideNo)){
            PesticideDateDto pesticideDateDto = PesticideDateDto.builder()
                    .pesticideDateNo(p.getPesticideDateNo())
                    .pesticideDate(p.getPesticideDate())
                    .plantName(p.getPlant().getPlantName())
                    .pesticideName(p.getPesticide().getPesticideName())
                    .build();

            pesticideDateDtoList.add(pesticideDateDto);
        }

        return pesticideDateDtoList;
    }

    @Override
    public PesticideDateDto getPesticideDate(int pesticideDateNo) {
        PesticideDate pesticideDate = pesticideDateRepository.findById(pesticideDateNo).orElseThrow(NoSuchElementException::new);

        return PesticideDateDto.builder()
                .pesticideDateNo(pesticideDate.getPesticideDateNo())
                .pesticideDate(pesticideDate.getPesticideDate())
                .plantName(pesticideDate.getPlant().getPlantName())
                .pesticideName(pesticideDate.getPesticide().getPesticideName())
                .build();
    }

    @Override
    public PesticideDateDto addPesticideDate(PesticideDateDto pesticideDateDto) {
        Plant plant = plantRepository.findById(pesticideDateDto.getPlantNo()).orElseThrow(NoSuchElementException::new);
        Pesticide pesticide = pesticideRepository.findById(pesticideDateDto.getPesticideNo()).orElseThrow(NoSuchElementException::new);

        // 저장
        PesticideDate pesticideDate = pesticideDateRepository.save(
                PesticideDate.builder()
                .pesticideDate(pesticideDateDto.getPesticideDate())
                .plant(plant)
                .pesticide(pesticide)
                .build());

        return PesticideDateDto.builder()
                .pesticideDateNo(pesticideDate.getPesticideDateNo())
                .pesticideDate(pesticideDate.getPesticideDate())
                .plantName(pesticideDate.getPlant().getPlantName())
                .pesticideName(pesticideDate.getPesticide().getPesticideName())
                .build();
    }

    @Override
    public PesticideDateDto modifyPesticideDate(PesticideDateDto pesticideDateDto) {
        Plant plant = plantRepository.findById(pesticideDateDto.getPlantNo()).orElseThrow(NoSuchElementException::new);
        Pesticide pesticide = pesticideRepository.findById(pesticideDateDto.getPesticideNo()).orElseThrow(NoSuchElementException::new);
        PesticideDate prevPesticideDate = pesticideDateRepository.findById(pesticideDateDto.getPesticideNo()).orElseThrow(NoSuchElementException::new);

        // 수정
        PesticideDate pesticideDate = pesticideDateRepository.save(
                prevPesticideDate.update(pesticideDateDto.getPesticideDate(),
                        plant,
                        pesticide)
        );

        return PesticideDateDto.builder()
                .pesticideDateNo(pesticideDate.getPesticideDateNo())
                .pesticideDate(pesticideDate.getPesticideDate())
                .plantName(pesticideDate.getPlant().getPlantName())
                .pesticideName(pesticideDate.getPesticide().getPesticideName())
                .build();
    }

    @Override
    public void deletePesticideDate(int pesticideDateNo) {
        pesticideDateRepository.deleteById(pesticideDateNo);
    }
}
