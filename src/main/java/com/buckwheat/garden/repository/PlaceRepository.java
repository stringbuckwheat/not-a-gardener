package com.buckwheat.garden.repository;

import com.buckwheat.garden.data.entity.Place;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Integer> {
    /**
     * plantList를 포함(EntityGraph)
     * 장소는 추가 순서대로 반환한다
     * @param memberNo int (member 테이블의 pk이자 place 테이블의 fk)
     * @return 해당 멤버의 전체 장소 엔티티 리스트
     */
    @EntityGraph(attributePaths = {"plantList"}, type= EntityGraph.EntityGraphType.FETCH)
    List<Place> findByMember_memberNoOrderByCreateDate(int memberNo);

    /**
     * plantList를 포함
     * @param placeNo int
     * @return 한 장소의 정보 + 해당 장소의 식물 리스트
     */
    @EntityGraph(attributePaths = {"plantList"}, type= EntityGraph.EntityGraphType.FETCH)
    Optional<Place> findById(int placeNo);

    Optional<Place> findByPlaceNo(int placeNo);
}
