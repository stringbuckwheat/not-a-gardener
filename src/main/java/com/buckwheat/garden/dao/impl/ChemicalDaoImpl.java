package com.buckwheat.garden.dao.impl;

import com.buckwheat.garden.dao.ChemicalDao;
import com.buckwheat.garden.data.entity.Chemical;
import com.buckwheat.garden.data.entity.Watering;
import com.buckwheat.garden.repository.ChemicalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Repository
public class ChemicalDaoImpl implements ChemicalDao {
    private final ChemicalRepository chemicalRepository;

    @Override
    public List<Chemical> getChemicalListByMemberNo(int memberNo){
        return chemicalRepository.findByMember_memberNo(memberNo);
    }

    @Override
    public List<Watering> getWateringListByChemicalNo(int chemicalNo){
        Chemical chemical = chemicalRepository.findByChemicalNo(chemicalNo).orElseThrow(NoSuchElementException::new);
        return chemical.getWateringList();
    }

    @Override
    public Chemical save(Chemical chemical) {
        return chemicalRepository.save(chemical);
    }

    @Override
    public void deleteByChemicalNo(int chemicalNo) {
        chemicalRepository.deleteById(chemicalNo);
    }
}
