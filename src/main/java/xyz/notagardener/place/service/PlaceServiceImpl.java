package xyz.notagardener.place.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.notagardener.common.error.code.ExceptionCode;
import xyz.notagardener.common.error.exception.ResourceNotFoundException;
import xyz.notagardener.common.error.exception.UnauthorizedAccessException;
import xyz.notagardener.gardener.Gardener;
import xyz.notagardener.gardener.repository.GardenerRepository;
import xyz.notagardener.place.Place;
import xyz.notagardener.place.dto.PlaceCard;
import xyz.notagardener.place.dto.PlaceDto;
import xyz.notagardener.place.repository.PlaceRepository;
import xyz.notagardener.plant.plant.dto.PlantInPlace;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaceServiceImpl implements PlaceService {
    private final PlaceRepository placeRepository;
    private final GardenerRepository gardenerRepository;

    public Place getPlaceByPlaceIdAndGardenerId(Long placeId, Long gardenerId) {
        Place place = placeRepository.findByPlaceId(placeId)
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionCode.NO_SUCH_PLACE));

        // 소유자 확인
        if (!place.getGardener().getGardenerId().equals(gardenerId)) {
            throw new UnauthorizedAccessException(ExceptionCode.NOT_YOUR_PLACE);
        }

        return place;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlaceCard> getAll(Long gardenerId) {
        return placeRepository.findByGardener_GardenerIdOrderByCreateDate(gardenerId)
                .stream()
                .map(PlaceCard::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PlaceDto getDetail(Long placeId, Long gardenerId) {
        Place place = getPlaceByPlaceIdAndGardenerId(placeId, gardenerId);
        Long plantListSize = placeRepository.countPlantsByPlaceId(placeId);

        return new PlaceDto(place, plantListSize);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlantInPlace> getPlantsWithPaging(Long placeId, Pageable pageable) {
        return placeRepository.findPlantsByPlaceIdWithPage(placeId, pageable).stream()
                .map(PlantInPlace::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PlaceCard add(Long gardenerId, PlaceDto placeRequest) {
        Gardener gardener = gardenerRepository.getReferenceById(gardenerId);
        Place place = placeRepository.save(placeRequest.toEntityWith(gardener));

        return new PlaceCard(place);
    }

    @Override
    @Transactional
    public PlaceDto update(PlaceDto placeRequest, Long gardenerId) {
        Place place = getPlaceByPlaceIdAndGardenerId(placeRequest.getId(), gardenerId);

        place.update(
                placeRequest.getName(),
                placeRequest.getOption(),
                placeRequest.getArtificialLight()
        );

        return new PlaceDto(place);
    }

    @Override
    public void delete(Long placeId, Long gardenerId) {
        Place place = getPlaceByPlaceIdAndGardenerId(placeId, gardenerId);
        placeRepository.delete(place);
    }
}
