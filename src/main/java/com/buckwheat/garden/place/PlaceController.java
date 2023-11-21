package com.buckwheat.garden.place;

import com.buckwheat.garden.place.dto.PlaceCard;
import com.buckwheat.garden.place.dto.PlaceDto;
import com.buckwheat.garden.plant.plant.PlantInPlace;
import com.buckwheat.garden.gardener.token.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/place")
@RequiredArgsConstructor
public class PlaceController {
    private final PlaceService placeService;

    @GetMapping("")
    public List<PlaceCard> getAll(@AuthenticationPrincipal UserPrincipal user) {
        return placeService.getAll(user.getId());
    }

    @GetMapping("/{placeId}")
    public PlaceDto getDetail(@PathVariable Long placeId, @AuthenticationPrincipal UserPrincipal user) {
        return placeService.getDetail(placeId, user.getId());
    }

    @GetMapping("/{placeId}/plant")
    public List<PlantInPlace> getPlantsWithPaging(@PathVariable Long placeId, @PageableDefault(size = 10) Pageable pageable) {
        return placeService.getPlantsWithPaging(placeId, pageable);
    }

    @PostMapping("")
    public PlaceCard add(@RequestBody PlaceDto placeRequest, @AuthenticationPrincipal UserPrincipal user) {
        return placeService.add(user.getId(), placeRequest);
    }

    @PutMapping("/{placeId}")
    public PlaceDto modify(@RequestBody PlaceDto placeRequest, @AuthenticationPrincipal UserPrincipal user) {
        return placeService.update(placeRequest, user.getId());
    }

    @DeleteMapping("/{placeId}")
    public void delete(@PathVariable long placeId, @AuthenticationPrincipal UserPrincipal user) {
        placeService.delete(placeId, user.getId());
    }
}
