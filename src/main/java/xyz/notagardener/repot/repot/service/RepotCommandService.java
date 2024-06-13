package xyz.notagardener.repot.repot.service;

import org.springframework.transaction.annotation.Transactional;
import xyz.notagardener.repot.Repot;
import xyz.notagardener.repot.repot.dto.RepotRequest;

public interface RepotCommandService {
    @Transactional(readOnly = true)
    Repot getRepotByRepotIdAndGardenerId(Long repotId, Long gardenerId);

    @Transactional
    Repot addOne(RepotRequest repotRequest, Long gardenerId);

    @Transactional
    Repot update(RepotRequest repotRequest, Long gardenerId);
}
