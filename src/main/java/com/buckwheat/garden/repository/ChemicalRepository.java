package com.buckwheat.garden.repository;

import com.buckwheat.garden.data.entity.Chemical;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChemicalRepository extends JpaRepository<Chemical, Integer> {
    /**
     * 모든 FK lazy 로딩
     * @param memberNo
     * @return
     */
    List<Chemical> findByMember_memberNo(int memberNo);

    List<Chemical> findByMember_memberNoOrderByChemicalPeriodDesc(int memberNo);

    @EntityGraph(attributePaths = {"wateringList", "wateringList.plant", "wateringList.plant.place"}, type = EntityGraph.EntityGraphType.FETCH)
    Optional<Chemical> findByChemicalNo(int chemicalNo);
}
