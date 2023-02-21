package com.buckwheat.garden.controller;

import com.buckwheat.garden.data.dto.PlaceDto;
import com.buckwheat.garden.service.PlaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/place")
@RequiredArgsConstructor
public class PlaceController {
    private final PlaceService placeService;

    @GetMapping("")
    public List<PlaceDto> getPlaceList(@AuthenticationPrincipal User user){
        return placeService.getPlaceList(Integer.parseInt(user.getUsername()));
    }

    @GetMapping("{placeNo}")
    public PlaceDto getPlace(@PathVariable int placeNo){
        return placeService.getPlace(placeNo);
    }

    @PostMapping("")
    public PlaceDto addPlace(@RequestBody PlaceDto placeDto){
        return placeService.addOrUpdatePlace(placeDto);
    }

    @PutMapping("/{placeNo}")
    public PlaceDto updatePlace(@RequestBody PlaceDto placeDto){
        return placeService.addOrUpdatePlace(placeDto);
    }

    @DeleteMapping("/{placeNo}")
    public void deletePlace(@PathVariable int placeNo){
        placeService.deletePlace(placeNo);
    }
}
