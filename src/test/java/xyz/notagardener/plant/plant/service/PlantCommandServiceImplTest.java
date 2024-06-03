package xyz.notagardener.plant.plant.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import xyz.notagardener.common.error.code.ExceptionCode;
import xyz.notagardener.common.error.exception.ResourceNotFoundException;
import xyz.notagardener.common.error.exception.UnauthorizedAccessException;
import xyz.notagardener.gardener.Gardener;
import xyz.notagardener.gardener.repository.GardenerRepository;
import xyz.notagardener.place.Place;
import xyz.notagardener.place.repository.PlaceRepository;
import xyz.notagardener.place.dto.ModifyPlace;
import xyz.notagardener.plant.Plant;
import xyz.notagardener.plant.plant.dto.MediumType;
import xyz.notagardener.plant.plant.dto.PlantRequest;
import xyz.notagardener.plant.plant.repository.PlantRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("식물 수정 컴포넌트 테스트")
class PlantCommandServiceImplTest {
    @Mock
    private PlantRepository plantRepository;

    @Mock
    private PlaceRepository placeRepository;

    @Mock
    private GardenerRepository gardenerRepository;

    @InjectMocks
    private PlantCommandServiceImpl plantCommandService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("식물 저장: 성공")
    void save_WhenSuccess_ShouldReturnPlantEntity() {
        // Given
        Long gardenerId = 1L;
        Long placeId = 2L;

        PlantRequest request = PlantRequest.builder().name("새로운 식물").placeId(placeId).build();

        Gardener gardener = Gardener.builder().gardenerId(gardenerId).build();
        Place place = Place.builder().placeId(placeId).gardener(gardener).build();

        Plant savedPlant = Plant.builder().plantId(3L).name(request.getName()).gardener(gardener).place(place).build();

        when(placeRepository.findByPlaceId(placeId)).thenReturn(Optional.of(place));
        when(gardenerRepository.getReferenceById(gardenerId)).thenReturn(gardener);
        when(plantRepository.save(any())).thenReturn(savedPlant);

        // When
        Plant result = plantCommandService.save(gardenerId, request);

        // Then
        assertNotNull(result);
        assertNotNull(result.getPlantId());
        assertEquals(request.getName(), request.getName());
        assertEquals(request.getPlaceId(), request.getPlaceId());
    }

    @Test
    @DisplayName("식물 저장: 내 장소가 아님 - 실패")
    void save_WhenPlaceNotMine_ShouldThrowUnauthorizedAccessException() {
        // Given
        Long requesterId = 1L;
        Long placeId = 2L;
        Long ownerId = 3L;

        PlantRequest request = PlantRequest.builder().name("새로운 식물").placeId(placeId).build();

        Gardener owner = Gardener.builder().gardenerId(ownerId).build();
        Gardener requester = Gardener.builder().gardenerId(requesterId).build();
        Place place = Place.builder().placeId(placeId).gardener(owner).build();

        Plant savedPlant = Plant.builder().plantId(3L).name(request.getName()).gardener(owner).place(place).build();

        when(placeRepository.findByPlaceId(placeId)).thenReturn(Optional.of(place));
        when(gardenerRepository.getReferenceById(requesterId)).thenReturn(requester);
        when(plantRepository.save(any())).thenReturn(savedPlant);

        // When, Then
        Executable executable = () -> plantCommandService.save(requesterId, request);
        UnauthorizedAccessException e = assertThrows(UnauthorizedAccessException.class, executable);
    }

    @Test
    @DisplayName("식물 수정: 성공")
    void update_ShouldReturnUpdatedPlant() {
        // Given
        Long gardenerId = 1L;
        Long prevPlaceId = 2L;
        Long newPlaceId = 3L;
        Long plantId = 4L;

        PlantRequest request = PlantRequest.builder()
                .id(plantId)
                .name("식물 이름 수정")
                .placeId(newPlaceId)
                .medium(MediumType.SEMI_HYDROPONIC.getType())
                .species("새로운 식물 종")
                .recentWateringPeriod(5)
                .birthday(LocalDate.now())
                .build();

        Gardener gardener = Gardener.builder().gardenerId(gardenerId).build();
        Place prevPlace = Place.builder().placeId(prevPlaceId).gardener(gardener).build();
        Place newPlace = Place.builder().placeId(newPlaceId).gardener(gardener).build();

        // 기존 데이터를 담고 있음
        Plant plant = Plant.builder()
                .plantId(plantId)
                .name("기존 식물 이름")
                .species("기존 식물 종")
                .recentWateringPeriod(6)
                .earlyWateringPeriod(10)
                .medium(MediumType.SPHAGNUM_MOSS.getType())
                .birthday(LocalDate.now().minusYears(1))
                .conditionDate(LocalDate.now().minusDays(3))
                .postponeDate(LocalDate.now().minusDays(2))
                .createDate(LocalDateTime.now().minusMonths(6))
                .gardener(gardener)
                .place(prevPlace)
                .build();

        when(placeRepository.findByPlaceId(newPlaceId)).thenReturn(Optional.of(newPlace));
        when(plantRepository.findByPlantId(plantId)).thenReturn(Optional.of(plant));

        // When
        Plant result = plantCommandService.update(request, gardenerId);

        // Then
        assertNotNull(result);

        assertEquals(plantId, plantId);
        assertEquals(plant.getEarlyWateringPeriod(), result.getEarlyWateringPeriod());
        assertEquals(plant.getConditionDate(), result.getConditionDate());
        assertEquals(plant.getPostponeDate(), result.getPostponeDate());
        assertEquals(plant.getCreateDate(), result.getCreateDate());

        assertEquals(request.getName(), result.getName());
        assertEquals(request.getSpecies(), result.getSpecies());
        assertEquals(request.getRecentWateringPeriod(), result.getRecentWateringPeriod());
        assertEquals(request.getMedium(), result.getMedium());
        assertEquals(request.getBirthday(), result.getBirthday());
    }

    @Test
    @DisplayName("식물 수정: 내 식물이 아님 - 실패")
    void update_WhenPlantNotMine_ShouldThrowUnauthorizedAccessException() {
        // Given
        Long requesterId = 1L;
        Long plantOwnerId = 2L;
        Long placeId = 3L;
        Long plantId = 4L;

        PlantRequest request = PlantRequest.builder()
                .id(plantId)
                .name("식물 이름 수정")
                .placeId(placeId)
                .medium(MediumType.SEMI_HYDROPONIC.getType())
                .build();

        Gardener requester = Gardener.builder().gardenerId(requesterId).build();
        Gardener owner = Gardener.builder().gardenerId(plantOwnerId).build();
        Place place = Place.builder().placeId(placeId).gardener(requester).build();

        // 기존 식물 엔티티
        Plant plant = Plant.builder()
                .plantId(plantId)
                .name("기존 식물 이름")
                .medium(MediumType.SPHAGNUM_MOSS.getType())
                .createDate(LocalDateTime.now().minusMonths(6))
                .gardener(owner)
                .place(place)
                .build();

        when(placeRepository.findByPlaceId(placeId)).thenReturn(Optional.of(place));
        when(plantRepository.findByPlantId(plantId)).thenReturn(Optional.of(plant));

        // When
        Executable executable = () -> plantCommandService.update(request, requesterId);
        UnauthorizedAccessException e = assertThrows(UnauthorizedAccessException.class, executable);
        assertEquals(ExceptionCode.NOT_YOUR_PLANT, e.getCode());
    }

    @Test
    @DisplayName("식물 수정: 그런 식물 없음 - 실패")
    void update_WhenPlantNotExist_ShouldThrowResourceNotFoundException() {
        // Given
        Long gardenerId = 1L;
        Long placeId = 2L;
        Long plantId = 3L;

        PlantRequest request = PlantRequest.builder()
                .id(plantId)
                .name("식물 이름 수정")
                .placeId(placeId)
                .medium(MediumType.SEMI_HYDROPONIC.getType())
                .build();

        Gardener gardener = Gardener.builder().gardenerId(gardenerId).build();
        Place place = Place.builder().placeId(placeId).gardener(gardener).build();

        when(placeRepository.findByPlaceId(placeId)).thenReturn(Optional.of(place));
        when(plantRepository.findByPlantId(plantId)).thenReturn(Optional.empty());

        // When
        Executable executable = () -> plantCommandService.update(request, gardenerId);
        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, executable);
    }

    @Test
    @DisplayName("식물 수정: 내 장소가 아님 - 실패")
    void update_WhenPlaceNotMine_ShouldThrowUnauthorizedAccessException() {
        // Given
        Long requesterId = 1L;
        Long placeOwnerId = 2L;
        Long prevPlaceId = 3L;
        Long newPlaceId = 4L;
        Long plantId = 4L;

        PlantRequest request = PlantRequest.builder().id(plantId).placeId(newPlaceId).build();

        Gardener requester = Gardener.builder().gardenerId(requesterId).build();
        Gardener placeOwner = Gardener.builder().gardenerId(placeOwnerId).build();
        Place prevPlace = Place.builder().placeId(prevPlaceId).gardener(requester).build();
        Place newPlace = Place.builder().placeId(newPlaceId).gardener(placeOwner).build();

        // 기존 식물 엔티티
        Plant plant = Plant.builder().plantId(plantId).gardener(requester).place(prevPlace).build();

        when(placeRepository.findByPlaceId(newPlaceId)).thenReturn(Optional.of(newPlace));
        when(plantRepository.findByPlantId(plantId)).thenReturn(Optional.of(plant));

        // When
        Executable executable = () -> plantCommandService.update(request, requesterId);
        UnauthorizedAccessException e = assertThrows(UnauthorizedAccessException.class, executable);
        assertEquals(ExceptionCode.NOT_YOUR_PLACE, e.getCode());
    }

    @Test
    @DisplayName("식물 수정: 그런 장소 없음 - 실패")
    void update_WhenPlaceNotExist_ShouldThrowResourceNotFoundException() {
        // Given
        Long gardenerId = 1L;
        Long placeId = 2L;
        Long plantId = 3L;

        PlantRequest request = PlantRequest.builder()
                .id(plantId)
                .name("식물 이름 수정")
                .placeId(placeId)
                .medium(MediumType.SEMI_HYDROPONIC.getType())
                .build();

        when(placeRepository.findByPlaceId(placeId)).thenReturn(Optional.empty());

        // When
        Executable executable = () -> plantCommandService.update(request, gardenerId);
        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, executable);
    }

    @Test
    @DisplayName("여러 식물 한 장소로 한 번에 수정하기: 성공")
    void updatePlantPlace_ShouldReturnPlaceEntity() {
        // Given
        Long gardenerId = 1L;
        Long placeId = 2L;

        List<Long> plantIds = Arrays.asList(4L, 5L);
        ModifyPlace request = new ModifyPlace(placeId, plantIds);

        Gardener gardener = Gardener.builder().gardenerId(gardenerId).build();
        Place place = Place.builder().placeId(placeId).gardener(gardener).build();

        when(placeRepository.findByPlaceId(placeId)).thenReturn(Optional.of(place));
        when(plantRepository.findByPlantId(plantIds.get(0)))
                .thenReturn(Optional.of(Plant.builder().plantId(plantIds.get(0)).gardener(gardener).build()));
        when(plantRepository.findByPlantId(plantIds.get(1)))
                .thenReturn(Optional.of(Plant.builder().plantId(plantIds.get(1)).gardener(gardener).build()));

        // When
        Place result = plantCommandService.updatePlantPlace(request, gardenerId);

        // Then
        assertNotNull(result);
    }

    @Test
    @DisplayName("여러 식물 한 장소로 한 번에 수정하기: 내 식물이 아님 - 실패")
    void updatePlantPlace_WhenPlantNotMine_ShouldThrowUnauthorizedAccessException() {
        // Given
        Long gardenerId = 1L;
        Long ownerId = 2L;
        Long prevPlaceId = 3L;
        Long newPlaceId = 4L;

        List<Long> plantIds = Arrays.asList(5L, 6L);
        ModifyPlace request = new ModifyPlace(newPlaceId, plantIds);

        Gardener gardener = Gardener.builder().gardenerId(gardenerId).build();
        Gardener owner = Gardener.builder().gardenerId(ownerId).build();
        Place prevPlace = Place.builder().placeId(prevPlaceId).gardener(gardener).build(); // 식물의 이전 장소
        Place newPlace = Place.builder().placeId(newPlaceId).gardener(gardener).build(); // 바꿀 장소

        List<Plant> plants = Arrays.asList(
                Plant.builder().place(prevPlace).plantId(plantIds.get(0)).gardener(gardener).build(),
                Plant.builder().place(prevPlace).plantId(plantIds.get(1)).gardener(owner).build()
        );

        when(placeRepository.findByPlaceId(newPlaceId)).thenReturn(Optional.of(newPlace));
        when(plantRepository.findByPlantId(plantIds.get(0))).thenReturn(Optional.of(plants.get(0)));
        when(plantRepository.findByPlantId(plantIds.get(1))).thenReturn(Optional.of(plants.get(1)));

        // When
        Executable executable = () -> plantCommandService.updatePlantPlace(request, gardenerId);
        UnauthorizedAccessException e = assertThrows(UnauthorizedAccessException.class, executable);
        assertEquals(ExceptionCode.NOT_YOUR_PLANT, e.getCode());
    }

    @Test
    @DisplayName("여러 식물 한 장소로 한 번에 수정하기: 그런 식물 없음 - 실패")
    void updatePlantPlace_WhenPlantNotExist_ShouldThrowResourceNotFoundException() {
        // Given
        Long gardenerId = 1L;
        Long prevPlaceId = 2L;
        Long newPlaceId = 3L;

        List<Long> plantIds = Arrays.asList(4L, 5L);
        ModifyPlace request = new ModifyPlace(newPlaceId, plantIds);

        Gardener gardener = Gardener.builder().gardenerId(gardenerId).build();
        Place prevPlace = Place.builder().placeId(prevPlaceId).gardener(gardener).build(); // 식물의 이전 장소
        Place newPlace = Place.builder().placeId(newPlaceId).gardener(gardener).build(); // 바꿀 장소

        List<Optional<Plant>> plants = Arrays.asList(
                Optional.of(Plant.builder().place(prevPlace).plantId(plantIds.get(0)).gardener(gardener).build()),
                Optional.empty()
        );

        when(placeRepository.findByPlaceId(newPlaceId)).thenReturn(Optional.of(newPlace));
        when(plantRepository.findByPlantId(plantIds.get(0))).thenReturn(plants.get(0));
        when(plantRepository.findByPlantId(plantIds.get(1))).thenReturn(plants.get(1));

        // When
        Executable executable = () -> plantCommandService.updatePlantPlace(request, gardenerId);
        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, executable);
    }

    @Test
    @DisplayName("여러 식물 한 장소로 한 번에 수정하기: 내 장소가 아님 - 실패")
    void updatePlantPlace_WhenPlaceNotMine_ShouldThrowUnauthorizedAccessException() {
        // Given
        Long gardenerId = 1L;
        Long ownerId = 2L;
        Long prevPlaceId = 3L;
        Long newPlaceId = 4L;

        List<Long> plantIds = Arrays.asList(5L, 6L);
        ModifyPlace request = new ModifyPlace(newPlaceId, plantIds);

        Gardener gardener = Gardener.builder().gardenerId(gardenerId).build();
        Gardener owner = Gardener.builder().gardenerId(ownerId).build();
        Place prevPlace = Place.builder().placeId(prevPlaceId).gardener(gardener).build(); // 식물의 이전 장소
        Place newPlace = Place.builder().placeId(newPlaceId).gardener(owner).build(); // 바꿀 장소

        when(placeRepository.findByPlaceId(newPlaceId)).thenReturn(Optional.of(newPlace));

        // When
        Executable executable = () -> plantCommandService.updatePlantPlace(request, gardenerId);
        UnauthorizedAccessException e = assertThrows(UnauthorizedAccessException.class, executable);
        assertEquals(ExceptionCode.NOT_YOUR_PLACE, e.getCode());
    }

    @Test
    @DisplayName("여러 식물 한 장소로 한 번에 수정하기: 그런 장소 없음 - 실패")
    void updatePlantPlace_WhenPlaceNotExist_ShouldThrowResourceNotFoundException() {
        // Given
        Long gardenerId = 1L;
        Long newPlaceId = 3L;

        List<Long> plantIds = Arrays.asList(4L, 5L);
        ModifyPlace request = new ModifyPlace(newPlaceId, plantIds);

        when(placeRepository.findByPlaceId(newPlaceId)).thenReturn(Optional.empty());

        // When
        Executable executable = () -> plantCommandService.updatePlantPlace(request, gardenerId);
        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, executable);
    }
}