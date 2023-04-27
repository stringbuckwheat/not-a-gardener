package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.dao.ChemicalDao;
import com.buckwheat.garden.dao.MemberDao;
import com.buckwheat.garden.data.dto.ChemicalDto;
import com.buckwheat.garden.data.dto.WateringDto;
import com.buckwheat.garden.data.entity.Chemical;
import com.buckwheat.garden.data.entity.Member;
import com.buckwheat.garden.data.entity.Watering;
import com.buckwheat.garden.service.ChemicalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChemicalServiceImpl implements ChemicalService {
    private final ChemicalDao chemicalDao;
    private final MemberDao memberDao;

    /**
     * 전체 chemical 리스트
     *
     * @param memberId int FK로 조회
     * @return dto로 변환한 chemical list
     */
    @Override
    public List<ChemicalDto.Response> getChemicalsByMemberId(Long memberId) {
        List<ChemicalDto.Response> chemicalList = new ArrayList<>();

        // entity -> dto
        for (Chemical chemical : chemicalDao.getChemicalsByMemberId(memberId)) {
            chemicalList.add(
                    ChemicalDto.Response.from(chemical)
            );
        }

        return chemicalList;
    }

    /**
     * 해당 chemical의 사용 내역
     *
     * @param chemicalId PK
     * @return 해당 chemical의 사용내역 리스트
     */
    @Override
    public List<WateringDto.ResponseInChemical> getWateringsByChemicalId(Long chemicalId) {
        List<WateringDto.ResponseInChemical> wateringList = new ArrayList<>();

        for (Watering watering : chemicalDao.getWateringsByChemicalId(chemicalId)) {
            wateringList.add(WateringDto.ResponseInChemical.from(watering));
        }

        return wateringList;
    }

    @Override
    public ChemicalDto.Response add(Long memberId, ChemicalDto.Request chemicalRequest) {
        Member member = memberDao.getMemberByMemberId(memberId).orElseThrow(NoSuchElementException::new);
        Chemical chemical = chemicalDao.save(chemicalRequest.toEntityWithMember(member));
        return ChemicalDto.Response.from(chemical);
    }

    @Override
    public ChemicalDto.Response modify(Long memberId, ChemicalDto.Request chemicalRequest) {
        Member member = memberDao.getMemberByMemberId(memberId).orElseThrow(NoSuchElementException::new);
        Chemical chemical = chemicalRequest.toEntityWithMemberForUpdate(member);
        chemicalDao.save(chemical);
        return ChemicalDto.Response.from(chemical);
    }

    @Override
    public void delete(Long id) {
        chemicalDao.deleteByChemicalId(id);
    }
}
