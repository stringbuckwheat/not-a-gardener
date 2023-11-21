package com.buckwheat.garden.domain.chemical;

import com.buckwheat.garden.domain.chemical.dto.ChemicalDetail;
import com.buckwheat.garden.domain.chemical.dto.ChemicalDto;
import com.buckwheat.garden.domain.chemical.service.ChemicalService;
import com.buckwheat.garden.domain.gardener.token.UserPrincipal;
import com.buckwheat.garden.domain.watering.dto.WateringResponseInChemical;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chemical")
@Slf4j
public class ChemicalController {
    private final ChemicalService chemicalService;

    @GetMapping("")
    public List<ChemicalDto> getAll(@AuthenticationPrincipal UserPrincipal user) {
        return chemicalService.getAll(user.getId());
    }

    @GetMapping("/{chemicalId}")
    public ChemicalDetail getDetail(@PathVariable Long chemicalId, @AuthenticationPrincipal UserPrincipal user) {
        return chemicalService.getOne(chemicalId, user.getId());
    }

    @GetMapping("/{chemicalId}/watering")
    public List<WateringResponseInChemical> getWateringWithPaging(@PathVariable Long chemicalId, @PageableDefault(size = 10) Pageable pageable) {
        return chemicalService.getWateringsForChemical(chemicalId, pageable);
    }

    @PostMapping("")
    public ChemicalDto add(@AuthenticationPrincipal UserPrincipal user, @RequestBody ChemicalDto chemicalRequest) {
        return chemicalService.add(user.getId(), chemicalRequest);
    }

    @PutMapping("/{chemicalId}")
    public ChemicalDto update(@AuthenticationPrincipal UserPrincipal user, @RequestBody ChemicalDto chemicalRequest) {
        return chemicalService.update(user.getId(), chemicalRequest);
    }

    @DeleteMapping("/{chemicalId}")
    public void delete(@PathVariable Long chemicalId, @AuthenticationPrincipal UserPrincipal user) {
        chemicalService.deactivate(chemicalId, user.getId());
    }
}
