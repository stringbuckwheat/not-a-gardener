package xyz.notagardener.common.model;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public class AuditableEntity {
    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdDate; // 생성 일자

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedDate; // 마지막 수정 일자
}