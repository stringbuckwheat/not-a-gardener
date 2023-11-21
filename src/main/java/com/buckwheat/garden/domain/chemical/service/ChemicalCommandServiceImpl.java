package com.buckwheat.garden.domain.chemical.service;

import com.buckwheat.garden.domain.chemical.Chemical;
import com.buckwheat.garden.domain.chemical.dto.ChemicalDto;
import com.buckwheat.garden.domain.chemical.repository.ChemicalRepository;
import com.buckwheat.garden.domain.chemical.repository.ServiceCallBack;
import com.buckwheat.garden.domain.gardener.Gardener;
import com.buckwheat.garden.domain.gardener.repository.GardenerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ChemicalCommandServiceImpl implements ChemicalCommandService{
    private final ChemicalRepository chemicalRepository;
    private final GardenerRepository gardenerRepository;

    @Override
    @Transactional
    public <T> T update(Long gardenerId, ChemicalDto chemicalRequest, ServiceCallBack<T> callBack) {
        Chemical chemical = chemicalRepository.findById(chemicalRequest.getId()).orElseThrow(NoSuchElementException::new);
        chemical.update(chemicalRequest.getName(), chemicalRequest.getType(), chemicalRequest.getPeriod());

        return callBack.execute(chemical);
    }

    @Override
    @Transactional
    public <T> T save(Long gardenerId, ChemicalDto chemicalRequest, ServiceCallBack<T> callBack) {
        Gardener gardener = gardenerRepository.getReferenceById(gardenerId);
        Chemical chemical = chemicalRepository.save(chemicalRequest.toEntity(gardener));

        // 서비스에서 제공하는 콜백을 호출하고 결과를 반환
        return callBack.execute(chemical);
    }
}
