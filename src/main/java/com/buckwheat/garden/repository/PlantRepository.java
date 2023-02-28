package com.buckwheat.garden.repository;

import com.buckwheat.garden.data.entity.Plant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlantRepository extends JpaRepository<Plant, Integer> {
    // FK로 조회하는 method 명명 규칙
    // findBy + [fk를 관리하는 entity의 필드명] + _ + [fk entity의 식별자 필드명]
    // List<Plant> findByMember_Username(String username);

    List<Plant> findByMember_MemberNo(int memberNo);
}
