package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.dao.PlantDao;
import com.buckwheat.garden.data.dto.GardenDto;
import com.buckwheat.garden.data.dto.PlaceDto;
import com.buckwheat.garden.data.dto.PlantDto;
import com.buckwheat.garden.data.entity.Member;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.service.PlantService;
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
    private final PlantDao plantDao;
    private final GardenResponseProvider gardenResponseProvider;

    /**
     * 하나의 장소 정보 반환
     *
     * @param plantNo
     * @return
     */
    @Override
    public PlantDto.Response getOnePlant(int plantNo) {
        return PlantDto.Response.from(plantDao.getPlantWithPlaceAndWatering(plantNo));
    }

    @Override
    public List<PlantDto.Response> getPlantList(int memberNo) {
        List<PlantDto.Response> plantList = new ArrayList<>();

        // @EntityGraph 메소드
        for (Plant p : plantDao.getPlantListByMemberNo(memberNo)) {
            plantList.add(PlantDto.Response.from(p));
        }

        return plantList;
    }

    @Override
    public GardenDto.Response addPlant(PlantDto.Request plantRequest, Member member) {
        Plant plant = plantDao.save(plantRequest, member.getMemberNo());
        return gardenResponseProvider.getGardenResponse(plant, member.getMemberNo());
    }

    @Override
    @Transactional
    public GardenDto.Response modifyPlant(PlantDto.Request plantRequest, Member member) {
        Plant plant = plantDao.update(plantRequest);
        return gardenResponseProvider.getGardenResponse(plant, member.getMemberNo());
    }

    @Override
    public PlaceDto.Response modifyPlantPlace(PlaceDto.ModifyPlantPlace modifyPlantPlaceDto) {
        return PlaceDto.Response.from(plantDao.updatePlantPlace(modifyPlantPlaceDto));
    }

    @Override
    public void deletePlantByPlantNo(int plantNo) {
        plantDao.deletePlantByPlantNo(plantNo);
    }
}
