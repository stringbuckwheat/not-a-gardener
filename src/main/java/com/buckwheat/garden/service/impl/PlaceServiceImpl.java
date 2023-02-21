package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.data.dto.PlaceDto;
import com.buckwheat.garden.data.entity.Member;
import com.buckwheat.garden.data.entity.Place;
import com.buckwheat.garden.repository.MemberRepository;
import com.buckwheat.garden.repository.PlaceRepository;
import com.buckwheat.garden.service.PlaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaceServiceImpl implements PlaceService {
    private final PlaceRepository placeRepository;
    private final MemberRepository memberRepository;

    // entity -> dto
    PlaceDto getPlaceDto(Place place){
        PlaceDto placeDto = new PlaceDto();

        placeDto.setPlaceNo(place.getPlaceNo());
        placeDto.setPlaceName(place.getPlaceName());
        placeDto.setOutside(place.getOutside());
        placeDto.setArtificialLight(place.getArtificialLight());
        placeDto.setMemberNo(place.getMember().getMemberNo());

        return placeDto;
    }

    @Override
    public List<PlaceDto> getPlaceList(int memberNo) {
        List<PlaceDto> list = new ArrayList<>();

        for(Place p : placeRepository.findByMember_memberNo(memberNo)){
            list.add(getPlaceDto(p));
        }

        return list;
    }

    @Override
    public PlaceDto getPlace(int placeNo) {
        Place place = placeRepository.findById(placeNo).orElseThrow(NoSuchElementException::new);
        return getPlaceDto(place);
    }

    @Override
    public PlaceDto addOrUpdatePlace(PlaceDto placeDto) {
        Member member = memberRepository.findById(placeDto.getMemberNo()).orElseThrow(NoSuchElementException::new);
        Place place = placeRepository.save(placeDto.toEntityWithMember(member));

        return getPlaceDto(place);
    }

    @Override
    public void deletePlace(int placeNo) {
        placeRepository.deleteById(placeNo);
    }
}
