package xyz.notagardener.place;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.notagardener.common.error.code.ExceptionCode;
import xyz.notagardener.common.error.exception.UnauthorizedAccessException;
import xyz.notagardener.gardener.Gardener;
import xyz.notagardener.gardener.gardener.GardenerRepository;
import xyz.notagardener.place.dto.PlaceCard;
import xyz.notagardener.place.dto.PlaceDto;
import xyz.notagardener.plant.dto.plant.PlantInPlace;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaceServiceImpl implements PlaceService {
    private final PlaceRepository placeRepository;
    private final GardenerRepository gardenerRepository;

    public Place getPlaceByPlaceIdAndGardenerId(Long placeId, Long gardenerId) {
        Place place = placeRepository.findByPlaceId(placeId)
                .orElseThrow(() -> new NoSuchElementException(ExceptionCode.NO_SUCH_ITEM.getCode()));

        // 소유자 확인
        if (!place.getGardener().getGardenerId().equals(gardenerId)) {
            throw new UnauthorizedAccessException(ExceptionCode.NOT_YOUR_THING.getCode());
        }

        return place;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlaceCard> getAll(Long gardenerId) {
        return placeRepository.findByGardener_GardenerIdOrderByCreateDate(gardenerId)
                .stream()
                .map(PlaceCard::from)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PlaceDto getDetail(Long placeId, Long gardenerId) {
        Place place = getPlaceByPlaceIdAndGardenerId(placeId, gardenerId);
        Long plantListSize = placeRepository.countPlantsByPlaceId(placeId);

        return PlaceDto.from(place, plantListSize);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlantInPlace> getPlantsWithPaging(Long placeId, Pageable pageable) {
        return placeRepository.findPlantsByPlaceIdWithPage(placeId, pageable).stream()
                .map(PlantInPlace::from)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PlaceCard add(Long gardenerId, PlaceDto placeRequest) {
        if (!placeRequest.isValidForSave()) {
            throw new IllegalArgumentException(ExceptionCode.INVALID_REQUEST_DATA.getCode());
        }

        Gardener gardener = gardenerRepository.getReferenceById(gardenerId);

        if (gardener == null) {
            throw new UsernameNotFoundException(ExceptionCode.NO_ACCOUNT.getCode());
        }

        Place place = placeRepository.save(placeRequest.toEntityWith(gardener));

        return PlaceCard.from(place);
    }

    @Override
    @Transactional
    public PlaceDto update(PlaceDto placeRequest, Long gardenerId) {
        if (!placeRequest.isValidForUpdate()) {
            throw new IllegalArgumentException(ExceptionCode.INVALID_REQUEST_DATA.getCode());
        }

        Place place = getPlaceByPlaceIdAndGardenerId(placeRequest.getId(), gardenerId);

        place.update(
                placeRequest.getName(),
                placeRequest.getOption(),
                placeRequest.getArtificialLight()
        );

        return PlaceDto.from(place);
    }

    @Override
    public void delete(Long placeId, Long gardenerId) {
        Place place = getPlaceByPlaceIdAndGardenerId(placeId, gardenerId);
        placeRepository.delete(place);
    }
}
