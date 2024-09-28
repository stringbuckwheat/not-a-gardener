package xyz.notagardener.place;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import xyz.notagardener.common.error.code.ExceptionCode;
import xyz.notagardener.common.error.exception.ResourceNotFoundException;
import xyz.notagardener.common.error.exception.UnauthorizedAccessException;
import xyz.notagardener.gardener.model.Gardener;
import xyz.notagardener.gardener.repository.GardenerRepository;
import xyz.notagardener.place.dto.PlaceCard;
import xyz.notagardener.place.dto.PlaceDto;
import xyz.notagardener.place.dto.PlaceType;
import xyz.notagardener.place.model.Place;
import xyz.notagardener.place.repository.PlaceRepository;
import xyz.notagardener.place.service.PlaceServiceImpl;
import xyz.notagardener.plant.model.Plant;
import xyz.notagardener.plant.plant.dto.PlantInPlace;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("장소 컴포넌트 테스트")
class PlaceServiceImplTest {
    @Mock
    private PlaceRepository placeRepository;

    @Mock
    private GardenerRepository gardenerRepository;

    @InjectMocks
    private PlaceServiceImpl placeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("한 장소 가져오기(+ 권한 검사)")
    void getPlaceByPlaceIdAndGardenerId_WhenValid_ReturnPlace() {
        // Given
        Long placeId = 1L;
        Long gardenerId = 2L;

        Gardener gardener = Gardener.builder().gardenerId(gardenerId).build();
        Place place = Place.builder().placeId(placeId).gardener(gardener).build();

        when(placeRepository.findByPlaceId(placeId)).thenReturn(Optional.of(place));

        // When
        Place result = placeService.getPlaceByPlaceIdAndGardenerId(placeId, gardenerId);

        // Then
        assertNotNull(result);
        assertEquals(placeId, result.getPlaceId());
    }

    @Test
    @DisplayName("한 장소 가져오기(+ 권한 검사): 그런 장소 없음 - 실패")
    void getPlaceByPlaceIdAndGardenerId_WhenPlaceNotExist_ThrowResourceNotFoundException() {
        // Given
        Long placeId = 1L;

        when(placeRepository.findByPlaceId(placeId)).thenReturn(Optional.empty());

        // When, Then
        Executable executable = () -> placeService.getPlaceByPlaceIdAndGardenerId(placeId, 1L);
        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, executable);
        assertEquals(ExceptionCode.NO_SUCH_PLACE, e.getCode());
    }

    @Test
    @DisplayName("한 장소 가져오기(+ 권한 검사): 내 장소가 아님 - 실패")
    void getPlaceByPlaceIdAndGardenerId_WhenNotYourPlace_ThrowUnauthorizedAccessException() {
        // Given
        Long placeId = 1L;
        Long gardenerId = 2L;
        Long ownerId = 3L;

        Gardener owner = Gardener.builder().gardenerId(ownerId).build();
        Place place = Place.builder().placeId(placeId).gardener(owner).build();

        when(placeRepository.findByPlaceId(placeId)).thenReturn(Optional.of(place));

        // When, Then
        Executable executable = () -> placeService.getPlaceByPlaceIdAndGardenerId(placeId, gardenerId);
        UnauthorizedAccessException e = assertThrows(UnauthorizedAccessException.class, executable);
        assertEquals(ExceptionCode.NOT_YOUR_PLACE, e.getCode());
    }

    @Test
    @DisplayName("한 회원의 장소 모두 가져오기")
    void getAll_ShouldReturnPlaceCardList() {
        // Given
        Long gardenerId = 1L;

        List<Place> places = new ArrayList<>();
        places.add(Place.builder().build());
        places.add(Place.builder().build());

        when(placeRepository.findByGardener_GardenerIdOrderByCreateDate(gardenerId)).thenReturn(places);

        // When
        List<PlaceCard> result = placeService.getAll(gardenerId);

        // Then
        assertEquals(places.size(), result.size());
    }

    @Test
    @DisplayName("한 장소의 상세 정보: 성공")
    void getDetail_ShouldReturnPlaceDto() {
        // Given
        Long placeId = 1L;
        Long gardenerId = 2L;

        Gardener gardener = Gardener.builder().gardenerId(gardenerId).build();
        Place place = Place.builder().placeId(placeId).gardener(gardener).build();

        when(placeRepository.findByPlaceId(placeId)).thenReturn(Optional.of(place));
        when(placeRepository.countPlantsByPlaceId(placeId)).thenReturn(5L);

        // When
        PlaceDto result = placeService.getDetail(placeId, gardenerId);

        // Then
        assertNotNull(result);
        assertEquals(placeId, result.getId());
    }

    @Test
    @DisplayName("한 장소의 상세 정보: 해당 장소 없음 - 실패")
    void getDetail_WhenPlaceNotExist_ShouldThrowResourceNotFoundException() {
        // Given
        Long placeId = 1L;

        when(placeRepository.findByPlaceId(placeId)).thenReturn(Optional.empty());

        // When, Then
        Executable executable = () -> placeService.getDetail(placeId, 1L);
        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, executable);
        assertEquals(ExceptionCode.NO_SUCH_PLACE, e.getCode());
    }

    @Test
    @DisplayName("한 장소의 상세 정보: 내 장소가 아님 - 실패")
    void getDetail_WhenPlaceNotMine_ShouldThrowUnauthorizedAccessException() {
        // Given
        Long placeId = 1L;
        Long gardenerId = 2L;
        Long ownerId = 3L;

        Gardener owner = Gardener.builder().gardenerId(ownerId).build();
        Place place = Place.builder().placeId(placeId).gardener(owner).build();

        when(placeRepository.findByPlaceId(placeId)).thenReturn(Optional.of(place));

        // When, Then
        Executable executable = () -> placeService.getDetail(placeId, gardenerId);
        UnauthorizedAccessException e = assertThrows(UnauthorizedAccessException.class, executable);
        assertEquals(ExceptionCode.NOT_YOUR_PLACE, e.getCode());
    }

    @Test
    @DisplayName("한 장소에 속한 식물(페이징)")
    void getPlantsWithPaging() {
        // Given
        Long placeId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        List<Plant> plants = List.of(
                Plant.builder().plantId(1L).createDate(LocalDateTime.now()).build(),
                Plant.builder().plantId(2L).createDate(LocalDateTime.now()).build()
        );

        when(placeRepository.findPlantsByPlaceIdWithPage(eq(placeId), any(Pageable.class))).thenReturn(plants);

        // When
        List<PlantInPlace> result = placeService.getPlantsWithPaging(placeId, pageable);

        // Then
        List<PlantInPlace> expected = plants.stream().map(PlantInPlace::new).collect(Collectors.toList());

        assertEquals(plants.size(), result.size());
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("장소 추가: 성공")
    void add_WhenValid_ReturnPlaceCard() {
        // Given
        Long gardenerId = 1L;
        PlaceDto request = PlaceDto.builder().name("새로운 장소").artificialLight("Y").option(PlaceType.INSIDE.getType()).build();

        Gardener gardener = Gardener.builder().gardenerId(gardenerId).build();
        Place savedPlace = Place.builder()
                .placeId(2L)
                .name(request.getName())
                .gardener(gardener)
                .artificialLight(request.getArtificialLight())
                .option(request.getOption())
                .createDate(LocalDateTime.now())
                .build();

        when(gardenerRepository.getReferenceById(gardenerId)).thenReturn(gardener);
        when(placeRepository.save(any())).thenReturn(savedPlace);

        // When
        PlaceCard result = placeService.add(gardenerId, request);

        // Then
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(new PlaceCard(savedPlace), result);
    }

    @Test
    @DisplayName("장소 수정: 성공")
    void update_WhenValid_ShouldReturnPlaceDto() {
        // Given
        Long gardenerId = 1L;
        PlaceDto request = PlaceDto.builder().id(1L).name("새로운 장소").artificialLight("Y").option(PlaceType.INSIDE.getType()).build();

        Gardener gardener = Gardener.builder().gardenerId(gardenerId).build();
        Place prevPlace = Place.builder()
                .placeId(request.getId())
                .name(request.getName())
                .gardener(gardener)
                .artificialLight(request.getArtificialLight())
                .option(request.getOption())
                .createDate(LocalDateTime.now())
                .build();

        when(placeRepository.findByPlaceId(request.getId())).thenReturn(Optional.of(prevPlace));

        // When
        PlaceDto result = placeService.update(request, gardenerId);

        // Then
        assertNotNull(result);
        assertEquals(new PlaceDto(prevPlace), result);
    }

    @Test
    @DisplayName("장소 수정: 그런 장소 없음")
    void update_WhenPlaceNotExist_ShouldThrowResourceNotFoundException() {
        // Given
        Long gardenerId = 1L;
        PlaceDto request = PlaceDto.builder().id(1L).name("새로운 장소").artificialLight("Y").option(PlaceType.INSIDE.getType()).build();

        when(placeRepository.findByPlaceId(request.getId())).thenReturn(Optional.empty());

        // When, Then
        Executable executable = () -> placeService.update(request, gardenerId);
        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, executable);
        assertEquals(ExceptionCode.NO_SUCH_PLACE, e.getCode());
    }

    @Test
    @DisplayName("장소 수정: 내 장소가 아님")
    void update_WhenPlaceNotMine_ShouldThrowUnauthorizedAccessException() {
        // Given
        Long gardenerId = 1L; // 입력값
        Long ownerId = 2L; // 실 소유주
        PlaceDto request = PlaceDto.builder().id(1L).name("새로운 장소").artificialLight("Y").option(PlaceType.INSIDE.getType()).build();

        Gardener owner = Gardener.builder().gardenerId(ownerId).build(); // 실제 소유자
        Place prevPlace = Place.builder().placeId(request.getId()).gardener(owner).build();

        when(placeRepository.findByPlaceId(request.getId())).thenReturn(Optional.of(prevPlace));

        // When, Then
        Executable executable = () -> placeService.update(request, gardenerId);
        UnauthorizedAccessException e = assertThrows(UnauthorizedAccessException.class, executable);
        assertEquals(ExceptionCode.NOT_YOUR_PLACE, e.getCode());
    }

    @Test
    @DisplayName("장소 삭제: 성공")
    void delete_WhenValid_ShouldDeletePlace() {
        // Given
        Long placeId = 1L;
        Long gardenerId = 1L;

        Gardener gardener = Gardener.builder().gardenerId(gardenerId).build();
        Place place = Place.builder().placeId(placeId).gardener(gardener).build();

        when(placeRepository.findByPlaceId(placeId)).thenReturn(Optional.of(place));

        // When
        placeService.delete(placeId, gardenerId);

        // Then
        verify(placeRepository, times(1)).delete(place);
    }

    @Test
    @DisplayName("장소 삭제: 그런 장소 없음")
    void delete_WhenPlaceNotExist_ShouldThrowResourceNotFoundException() {
        // Given
        Long placeId = 1L;
        Long gardenerId = 2L;

        when(placeRepository.findByPlaceId(placeId)).thenReturn(Optional.empty());

        // When, Then
        Executable executable = () -> placeService.delete(placeId, gardenerId);
        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, executable);
        assertEquals(ExceptionCode.NO_SUCH_PLACE, e.getCode());
    }

    @Test
    @DisplayName("장소 삭제: 내 장소가 아님")
    void delete_WhenPlaceNotMine_ShouldThrowUnauthorizedAccessException() {
        // Given
        Long placeId = 1L;
        Long gardenerId = 2L; // 요청자
        Long ownerId = 3L; // 소유자

        Gardener owner = Gardener.builder().gardenerId(ownerId).build();
        Place place = Place.builder().placeId(placeId).gardener(owner).build();

        when(placeRepository.findByPlaceId(placeId)).thenReturn(Optional.of(place));

        // When, Then
        Executable executable = () -> placeService.delete(placeId, gardenerId);
        UnauthorizedAccessException e = assertThrows(UnauthorizedAccessException.class, executable);
        assertEquals(ExceptionCode.NOT_YOUR_PLACE, e.getCode());
    }
}