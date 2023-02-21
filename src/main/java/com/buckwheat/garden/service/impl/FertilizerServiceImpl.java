package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.data.dto.FertilizerDto;
import com.buckwheat.garden.data.entity.Fertilizer;
import com.buckwheat.garden.data.entity.Member;
import com.buckwheat.garden.repository.FertilizerRepository;
import com.buckwheat.garden.repository.MemberRepository;
import com.buckwheat.garden.service.FertilizerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class FertilizerServiceImpl implements FertilizerService {
    private final FertilizerRepository fertilizerRepository;
    private final MemberRepository memberRepository;

    public FertilizerDto getFertilizerDto(Fertilizer fertilizer){
       return new FertilizerDto(
               fertilizer.getFertilizerNo(),
               fertilizer.getFertilizerName(),
               fertilizer.getFertilizingPeriod(),
               fertilizer.getFertilizerNo()
       );
    }

    @Override
    public List<FertilizerDto> getFertilizerList(int memberNo) {
        List<FertilizerDto> fertilizerList = new ArrayList<>();
        // entity -> dto
        for(Fertilizer f: fertilizerRepository.findByMember_memberNo(memberNo)){
            fertilizerList.add(getFertilizerDto(f));
        }

        return fertilizerList;
    }

    @Override
    public FertilizerDto addFertilizer(FertilizerDto fertilizerDto) {
        Member member = memberRepository.findById(fertilizerDto.getMemberNo()).orElseThrow(NoSuchElementException::new);
        Fertilizer fertilizer = fertilizerRepository.save(fertilizerDto.toEntityWithMember(member));

        return getFertilizerDto(fertilizer);
    }

    @Override
    public void deleteFertilizer(int fertilizerNo) {
        fertilizerRepository.deleteById(fertilizerNo);
    }
}
