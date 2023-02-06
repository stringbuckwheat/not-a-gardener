package com.buckwheat.garden.repository;

import com.buckwheat.garden.data.entity.Medium;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediumRepository extends JpaRepository<Medium, Integer> {
}
