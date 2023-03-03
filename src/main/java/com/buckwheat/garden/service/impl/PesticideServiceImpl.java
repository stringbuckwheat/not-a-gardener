package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.data.dto.PesticideDto;
import com.buckwheat.garden.data.entity.Member;
import com.buckwheat.garden.data.entity.Pesticide;
import com.buckwheat.garden.repository.PesticideRepository;
import com.buckwheat.garden.service.PesticideService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
@RequiredArgsConstructor
public class PesticideServiceImpl implements PesticideService {
    private final PesticideRepository pesticideRepository;

    private PesticideDto toDto(Pesticide pesticide){
        return PesticideDto.builder()
                .pesticideNo(pesticide.getPesticideNo())
                .pesticideName(pesticide.getPesticideName())
                .pesticideType(pesticide.getPesticideType())
                .pesticidePeriod(pesticide.getPesticidePeriod())
                .build();
    }

    @Override
    public List<PesticideDto> getPesticideList(int memberNo) {
        List<PesticideDto> pesticideList = new ArrayList<>();

        for(Pesticide p : pesticideRepository.findByMember_MemberNo(memberNo)){
            pesticideList.add(toDto(p));
        }

        return pesticideList;
    }

    @Override
    public PesticideDto getOnePesticide(int pesticideNo) {
        Pesticide pesticide = pesticideRepository.findById(pesticideNo).orElseThrow(NoSuchElementException::new);
        return toDto(pesticide);
    }

    @Override
    public PesticideDto addPesticide(PesticideDto pesticideDto, Member member) {
        Pesticide pesticide = pesticideRepository.save(Pesticide.builder()
                .pesticideName(pesticideDto.getPesticideName())
                .pesticideType(pesticideDto.getPesticideType())
                .pesticidePeriod(pesticideDto.getPesticidePeriod())
                .member(member)
                .build());

        return toDto(pesticide);
    }

    @Override
    public PesticideDto modifyPesticide(PesticideDto pesticideDto) {
        Pesticide pesticide = pesticideRepository.findById(pesticideDto.getPesticideNo()).orElseThrow(NoSuchElementException::new);

        pesticide.update(pesticideDto.getPesticideName(), pesticideDto.getPesticidePeriod());
        Pesticide paramPesticide = pesticideRepository.save(pesticide);

        return toDto(paramPesticide);
    }

    @Override
    public void deletePesticide(int pesticideNo) {
        pesticideRepository.deleteById(pesticideNo);
    }
}
