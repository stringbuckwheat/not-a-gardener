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
     * @param memberNo
     * @return
     * @Transactional X, EntityGraph로 한 번에 조회
     */
    @Override
    public List<Place> getPlaceListByMemberNo(int memberNo) {
        return placeRepository.findByMember_memberNoOrderByCreateDate(memberNo);
    }

    /**
     * EntityGraph
     *
     * @param placeNo
     * @return
     */
    @Override
    public Place getPlaceWithPlantList(int placeNo) {
        return placeRepository.findByPlaceNo(placeNo).orElseThrow(NoSuchElementException::new);
    }

    /**
     * Transactinal 필요
     *
     * @param placeRequestDto
     * @return
     */
    @Override
    public Place save(PlaceDto.PlaceRequestDto placeRequestDto, int memberNo) {
        Member member = memberRepository.findById(memberNo).orElseThrow(NoSuchElementException::new);
        return placeRepository.save(placeRequestDto.toEntityWith(member));
    }

    @Override
    public Place update(PlaceDto.PlaceRequestDto placeRequestDto) {
        Place place = placeRepository.findById(placeRequestDto.getPlaceNo()).orElseThrow(NoSuchElementException::new);

        return placeRepository.save(
                place.update(
                        placeRequestDto.getPlaceName(),
                        placeRequestDto.getOption(),
                        placeRequestDto.getArtificialLight()
                )
        );
    }

    @Override
    public void delete(int placeNo) {
        placeRepository.deleteById(placeNo);
    }
}
