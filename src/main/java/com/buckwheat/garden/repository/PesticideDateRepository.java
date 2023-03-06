package com.buckwheat.garden.repository;

import com.buckwheat.garden.data.entity.PesticideDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PesticideDateRepository extends JpaRepository<PesticideDate, Integer> {
    // 살충제별 살충제 관주/도포 날짜 리스트 (최신순)
    List<PesticideDate> findByPesticide_PesticideNoOrderByPesticideDateDesc(int pesticideNo);
}
