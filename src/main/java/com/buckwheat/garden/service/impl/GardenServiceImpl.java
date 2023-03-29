package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.data.dto.GardenDto;
import com.buckwheat.garden.data.dto.PlantDto;
import com.buckwheat.garden.data.entity.Chemical;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.repository.ChemicalRepository;
import com.buckwheat.garden.repository.PlantRepository;
import com.buckwheat.garden.service.GardenService;
import com.buckwheat.garden.util.GardenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GardenServiceImpl implements GardenService {
    private final PlantRepository plantRepository;
    private final ChemicalRepository chemicalRepository;
    private final GardenUtil gardenUtil;

    @Override
    public List<GardenDto.GardenResponse> getGarden(int memberNo) {
        List<GardenDto.GardenResponse> gardenList = new ArrayList<>();

        List<Chemical> chemicalList = chemicalRepository.findByMember_memberNoOrderByChemicalPeriodDesc(memberNo);
        List<Plant> plantList = plantRepository.findByMember_MemberNo(memberNo);

        // 필요한 것들 계산해서 gardenDto list 리턴
        for (Plant plant : plantList) {
            gardenList.add(getGardenResponse(plant, chemicalList));
        }

        return gardenList;
    }

    @Override
    public GardenDto.GardenResponse getGardenResponse(Plant plant, List<Chemical> chemicalList){
        PlantDto.PlantResponse plantResponse = PlantDto.PlantResponse.from(plant);
        GardenDto.GardenDetail gardenDetail = gardenUtil.getGardenDetail(plant, chemicalList);

        return new GardenDto.GardenResponse(plantResponse, gardenDetail);
    }
}
