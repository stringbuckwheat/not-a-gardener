package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.PesticideDto;
import com.buckwheat.garden.data.entity.Member;

import java.util.List;

public interface PesticideService {
    List<PesticideDto> getPesticideList(int memberNo);

    PesticideDto getOnePesticide(int pesticideNo);

    PesticideDto addPesticide(PesticideDto pesticideDto, Member member);

    PesticideDto modifyPesticide(PesticideDto pesticideDto);

    void deletePesticide(int pesticideNo);
}
