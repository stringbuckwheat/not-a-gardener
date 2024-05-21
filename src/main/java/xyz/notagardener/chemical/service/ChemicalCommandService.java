package xyz.notagardener.chemical.service;

import xyz.notagardener.chemical.Chemical;
import xyz.notagardener.chemical.dto.ChemicalDto;

public interface ChemicalCommandService {
    Chemical save(Long gardenerId, ChemicalDto chemicalRequest);
    Chemical update(Long gardenerId, ChemicalDto chemicalRequest);
}
