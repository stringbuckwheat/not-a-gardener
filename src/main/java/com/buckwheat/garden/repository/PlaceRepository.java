package com.buckwheat.garden.repository;

import com.buckwheat.garden.data.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Integer> {
    List<Place> findByMember_memberNo(int memberNo);
}
