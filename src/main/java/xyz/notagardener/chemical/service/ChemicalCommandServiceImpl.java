package xyz.notagardener.chemical.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.notagardener.chemical.Chemical;
import xyz.notagardener.chemical.dto.ChemicalDto;
import xyz.notagardener.chemical.repository.ChemicalRepository;
import xyz.notagardener.common.error.code.ExceptionCode;
import xyz.notagardener.common.error.exception.ResourceNotFoundException;
import xyz.notagardener.common.error.exception.UnauthorizedAccessException;
import xyz.notagardener.gardener.Gardener;
import xyz.notagardener.gardener.repository.GardenerRepository;

@Service
@RequiredArgsConstructor
public class ChemicalCommandServiceImpl implements ChemicalCommandService {
    private final ChemicalRepository chemicalRepository;
    private final GardenerRepository gardenerRepository;

    @Override
    @Transactional
    public Chemical update(Long gardenerId, ChemicalDto chemicalRequest) {
        Chemical chemical = chemicalRepository.findById(chemicalRequest.getId())
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionCode.NO_SUCH_CHEMICAL));

        if(!gardenerId.equals(chemical.getGardener().getGardenerId())) {
            throw new UnauthorizedAccessException(ExceptionCode.NOT_YOUR_CHEMICAL);
        }

        chemical.update(chemicalRequest.getName(), chemicalRequest.getType(), chemicalRequest.getPeriod());

        return chemical;
    }

    @Override
    @Transactional
    public Chemical save(Long gardenerId, ChemicalDto chemicalRequest) {
        Gardener gardener = gardenerRepository.getReferenceById(gardenerId);
        return chemicalRepository.save(chemicalRequest.toEntityWith(gardener));
    }
}
