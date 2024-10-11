package xyz.notagardener.common.model;

import xyz.notagardener.gardener.model.Gardener;

import java.time.LocalDateTime;

public interface Notifiable {
    Gardener getGardener();
    Long getId(); // ID
    LocalDateTime getCreatedDate(); // 생성 날짜
}
