package com.buckwheat.garden.watering;

import com.buckwheat.garden.data.entity.Watering;
import com.buckwheat.garden.data.projection.ChemicalUsage;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WateringQueryRepository extends Repository<Watering, Long>, WateringRepositoryCustom {

}
