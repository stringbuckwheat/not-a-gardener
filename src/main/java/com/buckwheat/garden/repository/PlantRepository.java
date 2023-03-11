package com.buckwheat.garden.repository;

import com.buckwheat.garden.data.entity.Plant;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.JoinColumn;
import java.util.List;
import java.util.Optional;

@Repository
public interface PlantRepository extends JpaRepository<Plant, Integer> {
    // FK로 조회하는 method 명명 규칙
    // findBy + [fk를 관리하는 entity의 필드명] + _ + [fk entity의 식별자 필드명]
    // List<Plant> findByMember_Username(String username);

    // EntityGraphType.FETCH: entity graph에 명시한 attribute는 eager, 나머지는 lazy
    // EntityGraphType.LOAD: entity graph에 명시한 attribute는 eager,
    // 나머지 attribute는 entity에 명시한 fetchType이나 디폴트 fetchType
    // ex. @OneToMany는 LAZY, @ManyToOne은 EAGER가 디폴트
    @EntityGraph(attributePaths = {"place"}, type = EntityGraph.EntityGraphType.FETCH)
    List<Plant> findByMember_MemberNoOrderByCreateDateDesc(int memberNo);

    /**
     * GardenController에서 식물 카드를 만들 데이터
     * @param memberNo
     * @return Place, WateringList가 매핑된 Plant 객체 리스트
     */
    @EntityGraph(attributePaths = {"place", "wateringList"}, type = EntityGraph.EntityGraphType.FETCH)
    List<Plant> findByMember_MemberNo(int memberNo);

    /**
     * Plant 페이지에서 식물 카드를 만들 데이터
     * @param plantNo
     * @return Place만 포함하는 plant 객체 하나
     */
    @EntityGraph(attributePaths = {"place", "wateringList"}, type= EntityGraph.EntityGraphType.FETCH)
    Optional<Plant> findByPlantNo(int plantNo);
}
