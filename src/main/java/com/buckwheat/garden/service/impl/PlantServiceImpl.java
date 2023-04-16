package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.dao.PlantDao;
import com.buckwheat.garden.data.dto.GardenDto;
import com.buckwheat.garden.data.dto.PlaceDto;
import com.buckwheat.garden.data.dto.PlantDto;
import com.buckwheat.garden.data.entity.Member;
import com.buckwheat.garden.data.entity.Plant;
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
    private final GardenUtil gardenUtil;
    private final PlantDao plantDao;

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

    public GardenDto.Response getGardenResponseFrom(int plantNo, int memberNo){
        Plant plant = plantDao.getPlantWithPlaceAndWatering(plantNo);

        PlantDto.Response plantResponse = PlantDto.Response.from(plant);
        GardenDto.Detail gardenDetail = gardenUtil.getGardenDetail(plant, memberNo);

        return new GardenDto.Response(plantResponse, gardenDetail);
    }

    @Override
    public GardenDto.Response addPlant(PlantDto.Request plantRequest, Member member) {
        Plant plant = plantDao.save(plantRequest, member.getMemberNo());
        return getGardenResponseFrom(plant.getPlantNo(), member.getMemberNo()); // TODO
    }

    @Override
    @Transactional
    public GardenDto.Response modifyPlant(PlantDto.Request plantRequest, Member member) {
        Plant plant = plantDao.update(plantRequest);

        // GardenDto를 돌려줘야 함
        PlantDto.Response plantResponse = PlantDto.Response.from(plant);
        GardenDto.Detail gardenDetail = gardenUtil.getGardenDetail(plant, member.getMemberNo());

        return new GardenDto.Response(plantResponse, gardenDetail);
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
