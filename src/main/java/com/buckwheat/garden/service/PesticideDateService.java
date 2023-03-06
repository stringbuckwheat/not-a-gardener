package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.PesticideDateDto;

import java.util.List;

public interface PesticideDateService {
    List<PesticideDateDto> getPesticideDateListByPesticide(int pesticideNo);

    PesticideDateDto getPesticideDate(int pesticideDateNo);

    PesticideDateDto addPesticideDate(PesticideDateDto pesticideDateDto);

    PesticideDateDto modifyPesticideDate(PesticideDateDto pesticideDateDto);

    void deletePesticideDate(int pesticideDateNo);
}
