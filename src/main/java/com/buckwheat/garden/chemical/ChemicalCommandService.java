package com.buckwheat.garden.chemical;

public interface ChemicalCommandService {
    <T> T save(Long gardenerId, ChemicalDto chemicalRequest, ServiceCallBack<T> callBack);
    <T> T update(Long gardenerId, ChemicalDto chemicalRequest, ServiceCallBack<T> callBack);
}
