package com.buckwheat.garden.repository;

import com.buckwheat.garden.data.entity.Place;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Integer> {
    @EntityGraph(attributePaths = {"plantList"}, type= EntityGraph.EntityGraphType.FETCH)
    List<Place> findByMember_memberNo(int memberNo);

    @EntityGraph(attributePaths = {"plantList"}, type= EntityGraph.EntityGraphType.FETCH)
    Optional<Place> findById(int placeNo);

    Optional<Place> findByPlaceNo(int placeNo);
}
