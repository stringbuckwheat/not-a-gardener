package xyz.notagardener.chemical.service;

import xyz.notagardener.chemical.dto.ChemicalDto;
import xyz.notagardener.common.ServiceCallBack;

public interface ChemicalCommandService {
    <T> T save(Long gardenerId, ChemicalDto chemicalRequest, ServiceCallBack<T> callBack);
    <T> T update(Long gardenerId, ChemicalDto chemicalRequest, ServiceCallBack<T> callBack);
}
