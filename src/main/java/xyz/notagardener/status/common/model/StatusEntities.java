package xyz.notagardener.status.common.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

// 서비스 간 교환 DTO
@AllArgsConstructor
@Getter
public class StatusEntities {
    private Status status;
    private StatusLog statusLog;
}
