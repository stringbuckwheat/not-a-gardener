package xyz.notagardener.domain.chemical.service;

import xyz.notagardener.domain.chemical.dto.ChemicalDto;
import xyz.notagardener.domain.chemical.repository.ServiceCallBack;

public interface ChemicalCommandService {
    <T> T save(Long gardenerId, ChemicalDto chemicalRequest, ServiceCallBack<T> callBack);
    <T> T update(Long gardenerId, ChemicalDto chemicalRequest, ServiceCallBack<T> callBack);
}
