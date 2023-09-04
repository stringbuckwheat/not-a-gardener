package com.buckwheat.garden.repository;

import com.buckwheat.garden.data.projection.RawGarden;
import com.buckwheat.garden.data.entity.Plant;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlantRepository extends JpaRepository<Plant, Long> {
    // FK로 조회하는 method 명명 규칙
    // findBy + [fk를 관리하는 entity의 필드명] + _ + [fk entity의 식별자 필드명]
    // List<Plant> findByGardener_Username(String username);

    // EntityGraphType.FETCH: entity graph에 명시한 attribute는 eager, 나머지는 lazy
    // EntityGraphType.LOAD: entity graph에 명시한 attribute는 eager,
    // 나머지 attribute는 entity에 명시한 fetchType이나 디폴트 fetchType
    // ex. @OneToMany는 LAZY, @ManyToOne은 EAGER가 디폴트
    @EntityGraph(attributePaths = {"place"}, type = EntityGraph.EntityGraphType.FETCH)
    List<Plant> findByGardener_GardenerIdOrderByCreateDateDesc(Long gardenerId);

    /**
     * GardenController에서 식물 카드를 만들 데이터
     * @param gardenerId
     * @return Place, WateringList가 매핑된 Plant 객체 리스트
     */
    @EntityGraph(attributePaths = {"place", "waterings", "waterings.chemical"}, type = EntityGraph.EntityGraphType.FETCH)
    List<Plant> findByGardener_GardenerId(Long gardenerId);

    @EntityGraph(attributePaths = {"place", "waterings", "waterings.chemical"}, type = EntityGraph.EntityGraphType.FETCH)
    Optional<Plant> findByPlantIdAndGardener_GardenerId(Long plantId, Long gardenerId);

    /**
     * Plant 페이지에서 식물 카드를 만들 데이터
     * @param plantId
     * @return Place만 포함하는 plant 객체 하나
     */
    @EntityGraph(attributePaths = {"place", "waterings", "waterings.chemical"}, type= EntityGraph.EntityGraphType.FETCH)
    Optional<Plant> findByPlantId(Long plantId);

    // 메인페이지 todolist 용 메소드
    @Query(value = "SELECT p.plant_id plantId, p.name name, p.species, p.recent_watering_period recentWateringPeriod, " +
            " p.early_watering_period earlyWateringPeriod, p.medium, p.birthday, p.condition_date conditionDate, " +
            " p.create_date createDate, max(watering_date) latestWateringDate, p.postpone_date postponeDate, " +
            " pl.place_id placeId, pl.name placeName" +
            " FROM plant p" +
            " INNER JOIN place pl" +
            " ON p.place_id = pl.place_id" +
            " LEFT JOIN watering w" +
            " ON p.plant_id = w.plant_id" +
            " WHERE p.gardener_id = :gardenerId" +
            " AND (DATE_FORMAT(p.condition_date, '%Y-%m-%d') != CURDATE() OR condition_date IS NULL)" +
            " AND p.recent_watering_period != 0 " +
            " GROUP BY p.plant_id" +
            " HAVING (MAX(watering_date) != CURDATE() or MAX(watering_date) IS NOT NULL)" +
            " OR DATEDIFF(MAX(watering_date), CURDATE()) >= 2" +
            " ORDER BY p.recent_watering_period DESC", nativeQuery = true)
    List<RawGarden> findGardenByGardenerId(@Param("gardenerId") Long gardenerId);

    // waitinglist
    @Query(value = "SELECT * FROM plant p" +
            " INNER JOIN place pl ON p.place_id = pl.place_id" +
            " LEFT JOIN watering w ON p.plant_id = w.plant_id" +
            " WHERE p.gardener_id = :gardenerId AND w.plant_id IS NULL", nativeQuery = true)
    List<Plant> findWaitingForWateringList(@Param("gardenerId") Long gardenerId);
}
