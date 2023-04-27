package com.buckwheat.garden.dao.impl;

import com.buckwheat.garden.dao.PlaceDao;
import com.buckwheat.garden.data.dto.PlaceDto;
import com.buckwheat.garden.data.entity.Member;
import com.buckwheat.garden.data.entity.Place;
import com.buckwheat.garden.repository.MemberRepository;
import com.buckwheat.garden.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.NoSuchElementException;

@Repository
@RequiredArgsConstructor
public class PlaceDaoImpl implements PlaceDao {
    private final PlaceRepository placeRepository;
    private final MemberRepository memberRepository;

    /**
     * @param memberId
     * @return
     * @Transactional X, EntityGraph로 한 번에 조회
     */
    @Override
    public List<Place> getPlacesByMemberId(Long memberId) {
        return placeRepository.findByMember_MemberIdOrderByCreateDate(memberId);
    }

    @Override
    public Place getPlaceWithPlantList(Long placeId) {
        return placeRepository.findByPlaceId(placeId).orElseThrow(NoSuchElementException::new);
    }

    /**
     * Transactinal 필요
     *
     * @param placeRequest
     * @return
     */
    @Override
    public Place save(Long memberId, PlaceDto.Request placeRequest) {
        Member member = memberRepository.findById(memberId).orElseThrow(NoSuchElementException::new);
        return placeRepository.save(placeRequest.toEntityWith(member));
    }

    @Override
    public Place update(PlaceDto.Request placeRequest) {
        Place place = placeRepository.findById(placeRequest.getId()).orElseThrow(NoSuchElementException::new);

        return placeRepository.save(
                place.update(
                        placeRequest.getName(),
                        placeRequest.getOption(),
                        placeRequest.getArtificialLight()
                )
        );
    }

    @Override
    public void deleteBy(Long id) {
        placeRepository.deleteById(id);
    }
}
