package com.buckwheat.garden.domain.chemical.service;

import com.buckwheat.garden.domain.chemical.dto.ChemicalDto;
import com.buckwheat.garden.domain.chemical.repository.ServiceCallBack;

public interface ChemicalCommandService {
    <T> T save(Long gardenerId, ChemicalDto chemicalRequest, ServiceCallBack<T> callBack);
    <T> T update(Long gardenerId, ChemicalDto chemicalRequest, ServiceCallBack<T> callBack);
}
