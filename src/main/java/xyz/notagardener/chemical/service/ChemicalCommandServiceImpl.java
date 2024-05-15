package xyz.notagardener.chemical.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.notagardener.chemical.Chemical;
import xyz.notagardener.chemical.dto.ChemicalDto;
import xyz.notagardener.chemical.repository.ChemicalRepository;
import xyz.notagardener.common.ServiceCallBack;
import xyz.notagardener.common.error.code.ExceptionCode;
import xyz.notagardener.common.error.exception.UnauthorizedAccessException;
import xyz.notagardener.gardener.Gardener;
import xyz.notagardener.gardener.gardener.GardenerRepository;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ChemicalCommandServiceImpl implements ChemicalCommandService {
    private final ChemicalRepository chemicalRepository;
    private final GardenerRepository gardenerRepository;

    @Override
    @Transactional
    public <T> T update(Long gardenerId, ChemicalDto chemicalRequest, ServiceCallBack<T> callBack) {
        Chemical chemical = chemicalRepository.findById(chemicalRequest.getId())
                .orElseThrow(() -> new NoSuchElementException(ExceptionCode.NO_SUCH_ITEM.getCode()));

        if (!chemical.getGardener().getGardenerId().equals(gardenerId)) {
            throw new UnauthorizedAccessException(ExceptionCode.NOT_YOUR_THING.getCode());
        }

        chemical.update(chemicalRequest.getName(), chemicalRequest.getType(), chemicalRequest.getPeriod());

        return callBack.execute(chemical);
    }

    @Override
    @Transactional
    public <T> T save(Long gardenerId, ChemicalDto chemicalRequest, ServiceCallBack<T> callBack) {
        Gardener gardener = gardenerRepository.getReferenceById(gardenerId);

        if(gardener == null) {
            throw new UsernameNotFoundException(ExceptionCode.NO_ACCOUNT.getCode());
        }

        Chemical chemical = chemicalRepository.save(chemicalRequest.toEntity(gardener));

        // 서비스에서 제공하는 콜백을 호출하고 결과를 반환
        return callBack.execute(chemical);
    }
}
