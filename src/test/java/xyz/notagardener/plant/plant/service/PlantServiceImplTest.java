package xyz.notagardener.plant.plant.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import xyz.notagardener.common.error.exception.ResourceNotFoundException;
import xyz.notagardener.common.error.exception.UnauthorizedAccessException;
import xyz.notagardener.gardener.Gardener;
import xyz.notagardener.place.Place;
import xyz.notagardener.plant.Plant;
import xyz.notagardener.plant.garden.dto.GardenDetail;
import xyz.notagardener.plant.garden.dto.GardenResponse;
import xyz.notagardener.plant.garden.dto.PlantResponse;
import xyz.notagardener.plant.garden.service.GardenResponseMapper;
import xyz.notagardener.plant.garden.service.PlantResponseFactory;
import xyz.notagardener.plant.garden.service.WateringCode;
import xyz.notagardener.plant.plant.dto.PlantRequest;
import xyz.notagardener.plant.plant.repository.PlantRepository;
import xyz.notagardener.watering.Watering;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@SpringBootTest
class PlantServiceImplTest {
    @MockBean
    private PlantCommandService plantCommandService;

    @MockBean
    private PlantRepository plantRepository;

    @Autowired
    private GardenResponseMapper gardenResponseMapper;

    @Autowired
    private PlantService plantService;

    private PlantResponseFactory rawGardenFactory = new PlantResponseFactory();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("전체 식물 정보(계산X): 성공")
    void getAll_ShouldReturnsPlantResponses() {
        // Given
        Long gardenerId = 1L;

        Gardener gardener = Gardener.builder().gardenerId(gardenerId).build();
        Place place = Place.builder().placeId(2L).build();

        List<Watering> waterings = Arrays.asList(
                Watering.builder().wateringId(1L).wateringDate(LocalDate.now()).build(),
                Watering.builder().wateringId(2L).wateringDate(LocalDate.now().minusDays(3)).build()
        );

        List<Plant> plants = Arrays.asList(
                Plant.builder().plantId(1L).name("Plant 1").gardener(gardener).waterings(new ArrayList<>()).place(place).createDate(LocalDateTime.now()).build(),
                Plant.builder().plantId(2L).name("Plant 2").gardener(gardener).waterings(waterings).place(place).createDate(LocalDateTime.now()).build()
        );

        when(plantRepository.findByGardener_GardenerIdOrderByPlantIdDesc(gardenerId)).thenReturn(plants);

        // When
        List<PlantResponse> plantResponses = plantService.getAll(gardenerId);

        // Then
        assertEquals(plants.size(), plantResponses.size());
        assertEquals(plants.get(0).getPlantId(), plantResponses.get(0).getId());
        assertEquals(plants.get(0).getName(), plantResponses.get(0).getName());
        assertEquals(plants.get(1).getPlantId(), plantResponses.get(1).getId());
        assertEquals(plants.get(1).getName(), plantResponses.get(1).getName());
    }

    @Test
    @DisplayName("한 식물의 세부 정보: 성공")
    void getDetail_WhenPlantExistsAndOwnerValid_ShouldReturnPlantResponse() {
        // Given
        Long plantId = 1L;
        Long gardenerId = 1L;

        PlantResponse expectedResponse = (PlantResponse) rawGardenFactory.getThirstyPlant(3);

        when(plantRepository.findPlantWithLatestWateringDate(plantId, gardenerId))
                .thenReturn(Optional.of(expectedResponse));

        // When
        PlantResponse actualResponse = plantService.getDetail(plantId, gardenerId);

        // Then
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    @DisplayName("한 식물의 세부 정보: 그런 식물 없음 - 실패")
    void getDetail_WhenPlantNotExist_ThrowResourceNotFoundException() {
        // Given
        Long plantId = 1L;
        Long gardenerId = 1L;

        when(plantRepository.findPlantWithLatestWateringDate(plantId, gardenerId)).thenReturn(Optional.empty());

        // When, Then
        assertThrows(ResourceNotFoundException.class, () -> plantService.getDetail(plantId, gardenerId));
    }

    @Test
    @DisplayName("한 식물의 세부 정보: 내 식물 아님 - 실패")
    void getDetail_WhenPlantNotMine_ThrowResourceNotFoundException() {
        // Given
        Long plantId = 1L;
        Long gardenerId = 1L;

        when(plantRepository.findPlantWithLatestWateringDate(plantId, gardenerId)).thenReturn(Optional.empty());

        // When, Then
        assertThrows(ResourceNotFoundException.class, () -> plantService.getDetail(plantId, gardenerId));
    }

    @Test
    @DisplayName("새 식물 저장: 성공")
    void add_ShouldReturnGardenResponse() {
        // Given
        Long gardenerId = 1L;
        Long placeId = 2L;

        PlantRequest request = PlantRequest.builder().name("새로운 식물").placeId(placeId).build();

        Gardener gardener = Gardener.builder().gardenerId(gardenerId).name("메밀").build();
        Place place = Place.builder().placeId(placeId).build();
        Plant savedPlant = Plant.builder().plantId(3L).name(request.getName()).gardener(gardener).waterings(new ArrayList<>()).place(place).createDate(LocalDateTime.now()).build();

        when(plantCommandService.save(gardenerId, request)).thenReturn(savedPlant);

        // When
        GardenResponse result = plantService.add(gardenerId, request);

        // Then
        assertNotNull(result);
        assertEquals(GardenDetail.notEnoughRecord(savedPlant.getBirthday()), result.getGardenDetail());
        assertEquals(new PlantResponse(savedPlant), result.getPlant());
    }

    @Test
    @DisplayName("식물 수정: 성공")
    void update_ShouldReturnGardenResponse() {
        // Given
        Long gardenerId = 1L;
        Long newPlaceId = 2L;
        Long plantId = 3L;

        // 식물 이름, 장소 수정한다고 가정
        PlantRequest request = PlantRequest.builder().id(plantId).name("식물 수정").placeId(newPlaceId).build();

        List<Watering> waterings = Arrays.asList(
                Watering.builder().wateringId(1L).wateringDate(LocalDate.now().minusDays(2)).build(),
                Watering.builder().wateringId(2L).wateringDate(LocalDate.now().minusDays(5)).build(),
                Watering.builder().wateringId(3L).wateringDate(LocalDate.now().minusDays(8)).build()
        );

        Gardener gardener = Gardener.builder().gardenerId(gardenerId).name("메밀").build();
        Place place = Place.builder().placeId(newPlaceId).build();
        Plant updatedPlant = Plant.builder().plantId(plantId).name(request.getName()).recentWateringPeriod(3).gardener(gardener).waterings(waterings).place(place).createDate(LocalDateTime.now()).build();

        when(plantCommandService.update(request, gardenerId)).thenReturn(updatedPlant);

        // When
        GardenResponse result = plantService.update(gardenerId, request);

        // Then
        assertNotNull(result);
        assertEquals(request.getName(), result.getPlant().getName());
        assertEquals(request.getPlaceId(), result.getPlant().getPlaceId());

        assertEquals(new PlantResponse(updatedPlant), result.getPlant());
        assertEquals(WateringCode.CHECK.getCode(), result.getGardenDetail().getWateringCode());
    }

    @Test
    @DisplayName("삭제: 성공")
    void delete_Success() {
        // Given
        Long gardenerId = 1L;
        Long plantId = 2L;

        Gardener gardener = Gardener.builder().gardenerId(gardenerId).build();
        Plant plant = Plant.builder().plantId(plantId).name("샌더소니 블루").gardener(gardener).build();
        when(plantRepository.findByPlantId(plantId)).thenReturn(Optional.of(plant));

        // When, Then
        assertDoesNotThrow(() -> plantService.delete(plantId, gardenerId));
        verify(plantRepository, times(1)).delete(plant);
    }

    @Test
    @DisplayName("식물 삭제: 그런 식물 없음 - 실패")
    void delete_WhenPlantNotFound_ShouldThrowResourceNotFoundException() {
        // Given
        Long gardenerId = 1L;
        Long plantId = 2L;
        when(plantRepository.findByPlantId(1L)).thenReturn(Optional.empty());

        // When, then
        assertThrows(ResourceNotFoundException.class, () -> plantService.delete(plantId, gardenerId));
    }

    @Test
    @DisplayName("식물 삭제: 내 식물 아님 - 실패")
    void delete_WhenPlantNotMine_ShouldThrowUnauthorizedAccessException() {
        Long requesterId = 1L;
        Long ownerId = 2L;
        Long plantId = 3L;

        Gardener owner = Gardener.builder().gardenerId(ownerId).build();
        Plant plant = Plant.builder().plantId(plantId).name("샌더소니 블루").gardener(owner).build();

        when(plantRepository.findByPlantId(plantId)).thenReturn(Optional.of(plant));

        assertThrows(UnauthorizedAccessException.class, () -> plantService.delete(plantId, requesterId));
    }
}