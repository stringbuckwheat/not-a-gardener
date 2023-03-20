package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.data.dto.ChemicalDto;
import com.buckwheat.garden.data.dto.WateringDto;
import com.buckwheat.garden.data.entity.Chemical;
import com.buckwheat.garden.data.entity.Member;
import com.buckwheat.garden.data.entity.Watering;
import com.buckwheat.garden.repository.ChemicalRepository;
import com.buckwheat.garden.repository.MemberRepository;
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
    private final ChemicalRepository chemicalRepository;
    private final MemberRepository memberRepository;

    @Override
    public List<ChemicalDto.ChemicalResponse> getChemicalList(int memberNo) {
        List<ChemicalDto.ChemicalResponse> chemicalList = new ArrayList<>();

        // entity -> dto
        for(Chemical chemical: chemicalRepository.findByMember_memberNo(memberNo)){
            chemicalList.add(
                    ChemicalDto.ChemicalResponse.from(chemical)
            );
        }

        return chemicalList;
    }

    @Override
    public List<WateringDto.WateringResponseInChemical> getWateringListByChemical(int chemicalNo) {
        // @EntityGraph attributePath: wateringList
        List<Watering> list = chemicalRepository.findByChemicalNo(chemicalNo).getWateringList();
        List<WateringDto.WateringResponseInChemical> wateringList = new ArrayList<>();

        for(Watering watering : list){
            wateringList.add(WateringDto.WateringResponseInChemical.from(watering));
        }

        log.debug("wateringList: {}", wateringList);
        return wateringList;
    }

    @Override
    public ChemicalDto.ChemicalResponse addChemical(ChemicalDto.ChemicalRequest chemicalRequest, Member member) {
        Chemical chemical = chemicalRepository.save(chemicalRequest.toEntityWithMember(member));
        return ChemicalDto.ChemicalResponse.from(chemical);
    }

    @Override
    public ChemicalDto.ChemicalResponse updateChemical(ChemicalDto.ChemicalRequest chemicalRequest, Member member) {
        Chemical chemical = chemicalRepository.save(chemicalRequest.toEntityWithMemberForUpdate(member));
        return ChemicalDto.ChemicalResponse.from(chemical);
    }

    @Override
    public void deleteChemical(int chemicalNo) {
        chemicalRepository.deleteById(chemicalNo);
    }
}
