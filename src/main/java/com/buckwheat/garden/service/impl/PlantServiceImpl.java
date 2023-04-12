package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.dao.PlantDao;
import com.buckwheat.garden.data.dto.GardenDto;
import com.buckwheat.garden.data.dto.PlaceDto;
import com.buckwheat.garden.data.dto.PlantDto;
import com.buckwheat.garden.data.entity.Member;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.repository.PlaceRepository;
import com.buckwheat.garden.repository.PlantRepository;
import com.buckwheat.garden.service.PlantService;
import com.buckwheat.garden.util.GardenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlantServiceImpl implements PlantService {
    private final PlantRepository plantRepository;
    private final PlaceRepository placeRepository;
    private final GardenUtil gardenUtil;
    private final PlantDao plantDao;

    /**
     * 하나의 장소 정보 반환
     *
     * @param plantNo
     * @return
     */
    @Override
    public PlantDto.PlantResponse getOnePlant(int plantNo) {
        return PlantDto.PlantResponse.from(plantDao.getPlantWithPlaceAndWatering(plantNo));
    }

    @Override
    public List<PlantDto.PlantResponse> getPlantList(int memberNo) {
        List<PlantDto.PlantResponse> plantList = new ArrayList<>();

        // @EntityGraph 메소드
        for (Plant p : plantDao.getPlantListByMemberNo(memberNo)) {
            plantList.add(PlantDto.PlantResponse.from(p));
        }

        return plantList;
    }

    public GardenDto.GardenResponse getGardenResponseFrom(int plantNo, int memberNo){
        Plant plant = plantDao.getPlantWithPlaceAndWatering(plantNo);

        PlantDto.PlantResponse plantResponse = PlantDto.PlantResponse.from(plant);
        GardenDto.GardenDetail gardenDetail = gardenUtil.getGardenDetail(plant, memberNo);

        return new GardenDto.GardenResponse(plantResponse, gardenDetail);
    }

    @Override
    public GardenDto.GardenResponse addPlant(PlantDto.PlantRequest plantRequestDto, Member member) {
        Plant plant = plantDao.save(plantRequestDto, member.getMemberNo());
        return getGardenResponseFrom(plant.getPlantNo(), member.getMemberNo()); // TODO
    }

    @Override
    @Transactional
    public GardenDto.GardenResponse modifyPlant(PlantDto.PlantRequest plantRequest, Member member) {
        Plant plant = plantDao.update(plantRequest);

        // GardenDto를 돌려줘야 함
        PlantDto.PlantResponse plantResponse = PlantDto.PlantResponse.from(plant);
        GardenDto.GardenDetail gardenDetail = gardenUtil.getGardenDetail(plant, member.getMemberNo());

        return new GardenDto.GardenResponse(plantResponse, gardenDetail);
    }

    @Override
    public PlaceDto.PlaceResponseDto modifyPlantPlace(PlaceDto.ModifyPlantPlaceDto modifyPlantPlaceDto) {
        return PlaceDto.PlaceResponseDto.from(plantDao.updatePlantPlace(modifyPlantPlaceDto));
    }

    @Override
    public void deletePlantByPlantNo(int plantNo) {
        plantDao.deletePlantByPlantNo(plantNo);
    }
}
